package com.gameshop.webservice;

import com.gameshop.model.GameConsole;
import org.hibernate.Session;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

@Secured
@Path("/consoles")
public class GameConsolePath {
    @GET
    @Produces("application/json")
    @Path("/")
    // --- This function returns the list of all consoles in the store (general resource) --- //
    public javax.ws.rs.core.Response getConsoles(
            @DefaultValue("10") @QueryParam("itemsPerPage") int itemsPerPage,
            @DefaultValue("1") @QueryParam("page") int page
    ) {
        try{
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Build query dynamically according to parameters values --- //
            String baseQuery = "FROM GameConsole gc";
            Map<String, Object> params = new HashMap<>();
            List<String> subQueries = new ArrayList<>();

            // --- Build queries --- //
            QueryPair queries = QueryBuilder.buildQueryPair(session, baseQuery, params, subQueries, page, itemsPerPage);

            // --- Get pagination information --- //
            PaginationInfo paginationInfo = new PaginationManager (page, itemsPerPage)
                    .buildPaginationInfo(queries.getCount());

            // --- Get result list --- //
            List consoles = queries.getQuery().getResultList();

            // --- Closing connection to DB --- //
            session.close();

            // --- Return HTTP Response --- //
            return Response.status(paginationInfo.getHttpCode())
                    .entity(buildConsoleListResponse(consoles, paginationInfo.getPaginationField()))
                    .build();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }
    }

    private String buildConsoleListResponse(List consoles, JsonObject paginationField) {
        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        // --- JSON Array of all the games --- //
        JsonArrayBuilder jsonConsoleList = Json.createArrayBuilder();
        for (int i = 0; i < consoles.size(); i++) {
            GameConsole console = (GameConsole)consoles.get(i);
            jsonConsoleList.add(Json.createObjectBuilder()
                    .add("name", console.getName())
                    .add("release_date", console.getReleaseDate().toString())
                    .add("model", console.getModel())
                    .add("description", console.getDescription())
                    .add("storage", console.getStorage())
                    .add("url", Json.createObjectBuilder()
                            .add("path", "/consoles/" + console.getName())));
        }

        // --- Add the JSON Array to the JSON response --- //
        return jsonResponse.add("list", jsonConsoleList.build())
                .add("pagination", paginationField)
                .build().toString();
    }

    @GET
    @Produces("application/json")
    @Path("/{name}")
    // --- This function returns the characteristics of a given console (specific resource) --- //
    public String getConsole(
            @PathParam("name") String idConsole
    ) {
        // --- DB management --- //
        Session session = getSessionFactory().openSession();

        // --- Query the given console's characteristics --- //
        String resultQuery = "from GameConsole gc where gc.name = :idConsole";
        GameConsole console = (GameConsole)session.createQuery(resultQuery)
                .setParameter("idConsole", idConsole)
                .uniqueResult();

        // --- Building response body --- //
        JsonObjectBuilder jsonResult = Json.createObjectBuilder();
        jsonResult.add("name", console.getName())
                .add("release_date", console.getReleaseDate().toString())
                .add("model", console.getModel())
                .add("description", console.getDescription())
                .add("storage", console.getStorage());

        return jsonResult.build().toString();
    }
}

