package com.gameshop.webservice;

import com.gameshop.model.Game;
import org.hibernate.Session;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

@Secured
@Path("/games")
public class GamePath {
    // Path to game list
    @GET
    @Produces("application/json")
    @Path("/")
    // --- This function returns the list of all games in the store (general resource) --- //
    public javax.ws.rs.core.Response getGames(
            @DefaultValue("10") @QueryParam("itemsPerPage") int itemsPerPage,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("-1") @QueryParam("priceMin") Double priceMin,
            @DefaultValue("100000") @QueryParam("priceMax") Double priceMax,
            @QueryParam("console") List <String> console,
            @QueryParam("age") List <Integer> age,
            @QueryParam("type") List <String> type,
            @QueryParam("rate") List <Double> rate
    ) {
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Build query dynamically according to parameters values --- //
            String baseQuery = "FROM Game g";
            Map<String, Object> params = new HashMap<>();
            List<String> subQueries = new ArrayList<>();
            if (console.size() != 0) {
                subQueries.add("g.console IN (:console)");
                params.put( "console", console);
            }
            if (age.size() != 0) {
                subQueries.add("g.ageLimit IN (:age)");
                params.put( "age", age);
            }
            if (type.size() != 0) {
                subQueries.add("g.id IN (SELECT idGame FROM Characterize c WHERE c.word IN (:type)) ");
                params.put( "type", type);
            }
            if (rate.size() != 0) {
                subQueries.add("g.rate IN (:rate) ");
                params.put( "rate", rate);
            }

            // --- Add price constraint (We add it no matter what happens since we set default values to parameters) --- //
            subQueries.add("g.price BETWEEN :priceMin AND :priceMax");
            params.put( "priceMin", priceMin);
            params.put( "priceMax", priceMax);

            // --- Build queries --- //
            QueryPair queries = QueryBuilder.buildQueryPair(session, baseQuery, params, subQueries, page, itemsPerPage);

            // --- Get pagination information --- //
            PaginationInfo paginationInfo = new PaginationManager(page, itemsPerPage)
                    .buildPaginationInfo(queries.getCount());

            // --- Get result list --- //
            List games = queries.getQuery().getResultList();

            // --- Closing connection to DB --- //
            session.close();

            // --- Return HTTP Response --- //
            return Response.status(paginationInfo.getHttpCode())
                    .entity(buildGameListResponse(games, paginationInfo.getPaginationField()))
                    .build();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }
    }

    private String buildGameListResponse(List games, JsonObject paginationField) {
        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        // -- Create a session to fetch stock of each games -- //
        Session session = getSessionFactory().openSession();

        // --- JSON Array of all the games --- //
        JsonArrayBuilder jsonGameList = Json.createArrayBuilder();
        for (int i = 0; i < games.size(); i++) {
            Game gres = (Game)games.get(i);

            // --- Query the stock for the given game --- //
            String stockQuery = "select count(*) from GameEntity g where g.idGame = :idGame and g.idPurchase = null";
            Long gameEntities = (Long)session.createQuery(stockQuery)
                    .setParameter("idGame", gres.getId())
                    .uniqueResult();

            // --- Query the keywords associated to the game --- //
            String keywordsQuery = "select word from Characterize c where c.idGame = :idGame";
            List keywords = session.createQuery(keywordsQuery)
                    .setParameter("idGame", gres.getId())
                    .getResultList();

            // Building keyword JSON object
            JsonArrayBuilder jsonKeywordList = Json.createArrayBuilder();
            for (int j = 0; j < keywords.size(); j++) {
                String keyword = (String)keywords.get(j);
                jsonKeywordList.add(Json.createObjectBuilder()
                        .add("word", keyword));
            }


            jsonGameList.add(Json.createObjectBuilder()
                    .add("id", gres.getId())
                    .add("title", gres.getTitle())
                    .add("console", gres.getConsole())
                    .add("description", gres.getDescription())
                    .add("price", gres.getPrice())
                    .add("rate", gres.getRate())
                    .add("stock", gameEntities)
                    .add("age", gres.getAgeLimit())
                    .add("release_date", gres.getReleaseDate().toString())
                    .add("keywords", jsonKeywordList)
                    .add("url", Json.createObjectBuilder()
                            .add("path", "/games/"+gres.getId())));
        }

        // --- Add the JSON Array to the JSON response --- //
        return jsonResponse.add("list", jsonGameList.build())
                .add("pagination", paginationField)
                .build().toString();
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    // --- This function returns the characteristics of a given game (specific resource)
    public javax.ws.rs.core.Response getGame(
            @PathParam("id") int idGame
    ) {
        // --- DB management --- //
        Session session = getSessionFactory().openSession();

        // --- Query the given game's characteristics --- //
        String resultQuery = "from Game g where g.id = :idGame";
        Game game = (Game)session.createQuery(resultQuery)
                .setParameter("idGame", idGame)
                .uniqueResult();

        // --- Query the keywords associated to the game --- //
        String keywordsQuery = "select word from Characterize c where c.idGame = :idGame";
        List keywords = session.createQuery(keywordsQuery)
                .setParameter("idGame", idGame)
                .getResultList();

        // --- Query the stock for the given game --- //
        String stockQuery = "select count(*) from GameEntity g where g.idGame = :idGame and g.idPurchase = null";
        Long gameEntities = (Long)session.createQuery(stockQuery)
                .setParameter("idGame", idGame)
                .uniqueResult();

        // Building keyword JSON object
        JsonArrayBuilder jsonKeywordList = Json.createArrayBuilder();
        for (int i = 0; i < keywords.size(); i++) {
            String keyword = (String)keywords.get(i);
            jsonKeywordList.add(Json.createObjectBuilder()
                    .add("word", keyword));
        }

        // --- Building response body --- //
        JsonObjectBuilder jsonResult = Json.createObjectBuilder();
        jsonResult.add("id", game.getId())
            .add("title", game.getTitle())
            .add("console", game.getConsole())
            .add("description", game.getDescription())
            .add("price", game.getPrice())
            .add("rate", game.getRate())
            .add("age", game.getAgeLimit())
            .add("url", Json.createObjectBuilder()
                    .add("path", "/games/"+game.getId()))
            .add("release_date", game.getReleaseDate().toString())
            .add("keywords", jsonKeywordList)
            .add("stock", gameEntities);

        return Response.status(200).entity(jsonResult.build().toString()).build();
    }
}
