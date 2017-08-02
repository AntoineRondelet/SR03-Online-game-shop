package com.gameshop.webservice;

import com.gameshop.model.Game;
import com.gameshop.model.GameEntity;
import com.gameshop.model.Purchase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.*;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;


@Secured
@Path("/purchases")
public class PurchasePath {
    @GET
    @Produces("application/json")
    @Path("/")
    // --- This function returns the list of all purchases made on the store --- //
    public javax.ws.rs.core.Response getPurchases(
            @DefaultValue("10") @QueryParam("itemsPerPage") int itemsPerPage,
            @DefaultValue("1") @QueryParam("page") int page,
            @QueryParam("username") List<String> username,
            @QueryParam("mode") List<String> mode,
            @QueryParam("status") List<String> status,
            @QueryParam("date") List<Date> date
    ){
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Build query dynamically according to parameters values --- //
            String baseQuery = "FROM Purchase p";
            Map<String, Object> params = new HashMap<>();
            List<String> subQueries = new ArrayList<>();
            if (username.size() != 0) {
                subQueries.add("p.username IN (:username)");
                params.put( "username", username);
            }
            if (mode.size() != 0) {
                subQueries.add("p.paymentMode IN (:mode)");
                params.put( "mode", mode);
            }
            if (status.size() != 0) {
                subQueries.add("p.paymentStatus IN (:status)");
                params.put( "status", status);
            }
            if (date.size() != 0) {
                subQueries.add("p.datePaid IN (:date) ");
                params.put( "date", date);
            }

            // --- Build queries --- //
            QueryPair queries = QueryBuilder.buildQueryPair(session, baseQuery, params, subQueries, page, itemsPerPage);

            // --- Get pagination information --- //
            PaginationInfo paginationInfo = new PaginationManager (page, itemsPerPage)
                    .buildPaginationInfo(queries.getCount());

            // --- Get result list --- //
            List purchase = queries.getQuery().getResultList();

            // --- Closing connection to DB --- //
            session.close();

            // --- Return HTTP Response --- //
            return Response.status(paginationInfo.getHttpCode())
                    .entity(buildPurchaseListResponse(purchase, paginationInfo.getPaginationField()))
                    .build();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }
    }

    private String buildPurchaseListResponse(List purchases, JsonObject paginationField) {
        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        // --- JSON Array of all the games --- //
        JsonArrayBuilder jsonPurchaseList = Json.createArrayBuilder();
        for (int i = 0; i < purchases.size(); i++) {
            Purchase purchase = (Purchase) purchases.get(i);
            jsonPurchaseList.add(Json.createObjectBuilder()
                    .add("id", purchase.getId())
                    .add("date_paid", purchase.getDatePaid().toString())
                    .add("customer", purchase.getUsername())
                    .add("payment_mode", purchase.getPaymentMode())
                    .add("payment_status", purchase.getPaymentStatus())
                    .add("url", Json.createObjectBuilder()
                            .add("path", "/purchases/"+ purchase.getId())));
        }

        // --- Add the JSON Array to the JSON response --- //
        return jsonResponse.add("list", jsonPurchaseList.build())
                .add("pagination", paginationField)
                .build().toString();
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    // --- This function returns the characteristics and the content of a given purchase (specific resource) --- //
    public javax.ws.rs.core.Response getGame(
            @PathParam("id") int idPurchase
    ){
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Query the given purchase's characteristics --- //
            String purchaseQuery = "FROM Purchase p WHERE p.id = :idPurchase";
            Purchase purchase = (Purchase) session.createQuery(purchaseQuery)
                    .setParameter("idPurchase", idPurchase)
                    .uniqueResult();

            // --- Query the game entities associated to the purchase --- //
            String gamesIdsQuery = "SELECT DISTINCT ge.idGame FROM GameEntity ge WHERE ge.idPurchase = :idPurchase";
            List gamesIds = session.createQuery(gamesIdsQuery)
                    .setParameter("idPurchase", idPurchase)
                    .getResultList();

            // -- If the cart/purchase is empty, we stop and return nothing -- //
            if (purchase == null) {
                return Response.status(200).entity("").build();
            }

            // --- For each game entity, we retrieve the characteristics of the Game --- //
            // Building keyword JSON object
            JsonArrayBuilder jsonGameList = Json.createArrayBuilder();
            for (int i = 0; i < gamesIds.size(); i++) {
                int game = (int) gamesIds.get(i);
                // --- Query the game characteristics --- //
                String gameQuery = "FROM Game g WHERE g.id = :idGame";
                Game gameCharacteristics = (Game) session.createQuery(gameQuery)
                        .setParameter("idGame", game)
                        .uniqueResult();

                // Query the table to have to gameEntities of the game that are being bought
                String gameEntitiesQuery = "SELECT ge.id FROM GameEntity ge WHERE ge.idPurchase = :idPurchase AND ge.idGame = :idGame";
                List gameEntities = session.createQuery(gameEntitiesQuery)
                        .setParameter("idPurchase", idPurchase)
                        .setParameter("idGame", game)
                        .getResultList();

                JsonArrayBuilder jsonInstancesList = Json.createArrayBuilder();
                for (int j = 0; j < gameEntities.size(); j++) {
                    jsonInstancesList.add(Json.createObjectBuilder().add("reference", (String) gameEntities.get(j)));
                }

                jsonGameList.add(Json.createObjectBuilder()
                        .add("game_id", gameCharacteristics.getId())
                        .add("game_title", gameCharacteristics.getTitle())
                        .add("game_price", gameCharacteristics.getPrice())
                        .add("console", gameCharacteristics.getConsole())
                        .add("instances", jsonInstancesList)
                );
            }

            JsonObjectBuilder jsonResult = Json.createObjectBuilder();
            jsonResult.add("id", purchase.getId())
                    .add("consumer", purchase.getUsername())
                    .add("payment_status", purchase.getPaymentStatus())
                    .add("games", jsonGameList.build());

            // -- If the payment has been done, then we return the whole data of the payment -- //
            if (!purchase.getPaymentStatus().equals("Waiting")) {
                jsonResult.add("date_paid", purchase.getDatePaid().toString())
                        .add("payment_mode", purchase.getPaymentMode());
            }

            return Response.status(200).entity(jsonResult.build().toString()).build();
        } catch (Exception e) {
            System.out.println("Handled internal error: " + e.toString());
            return Response.status(500).entity(e.toString()).build();
        }
    }

    @PUT
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/")
    // Utilisé pour ajouter et supprimer des jeux a notre panier
    // TODO: A voir si on le split en 2 differents endpoints pour eviter le champ action a l'objet JSON d'entree
    // L'input data est de la forme: {"cart": "IdDuCart", "game": "IdDuJeuAManipuler", "action": "add/delete"}
    public javax.ws.rs.core.Response setGame(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
            String inputData
    ){
        String cartID = "";
        String message = "";

        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Get input data --- //
            JSONObject inputJson = new JSONObject(inputData);

            // -- Extract the username from the token -- //
            String token = authorization.substring("Bearer".length()).trim();
            String user = new AuthManager().getSubject(token);

            if (!inputJson.get("action").equals("add") && !inputJson.get("action").equals("delete")) {
                throw new Exception("Invalid action in JSON data (GOT " + inputJson.get("action") + " )");
            }


            System.out.println("DEBUG 3");
            if (inputJson.get("action").equals("add")) {
                // Si l'action vaut "add", alors l'id vaut l'id du Game que l'on veut acheter
                // Le gameEntity choisit est le premier resultat de la requete SQL

                // On lit le cookie contenant l'id du panier
                // Si ce cookie est vide --> on le genere et on le renvoie a l'utilisateur
                // (apres avoir créer le purcahse dans la BDD et avoir mis le jeu souhaité dedans
                // Si il existe deja, alors on inclu juste le jeu que l'on souhaite dans le panier

                // Creation du purchase dans la BDD
                if (inputJson.get("cart").equals("-1")) {
                    System.out.println("DEBUG 2");
                    Purchase newPurchase = new Purchase();
                    newPurchase.setUsername(user);
                    newPurchase.setPaymentStatus("Waiting");
                    System.out.println("ID --> " + newPurchase.getId());
                    Serializable generatedId = session.save(newPurchase);
                    System.out.println("DEBUG 5 --> " + generatedId.toString());
                    cartID = generatedId.toString();
                } else {
                    System.out.println("DEBUG 7");
                    cartID = inputJson.get("cart").toString();
                }
                System.out.println("DEBUG 4");

                // We do the query on the DB and return only one result (or null) that matches the request
                // We select only a random instance of the game the the user wants to buy
                String gameQuery = "from GameEntity g where g.idPurchase is NULL AND g.idGame = (:idGame)";
                List games = session.createQuery(gameQuery)
                        .setParameter("idGame", Integer.parseInt(inputJson.get("game").toString()))
                        .getResultList();


                if (games == null || games.size() == 0) {
                    return Response.status(200)
                            .entity(Json.createObjectBuilder()
                                    .add("cart", cartID)
                                    .add("message", "Sorry... The game you want to buy is out of stock :(")
                                    .build().toString())
                            .build();
                }

                GameEntity game = (GameEntity) games.get(0);

                Transaction tx = session.beginTransaction();
                String addGame = "UPDATE GameEntity g SET g.idPurchase = (:newIdPurchase) " +
                        "WHERE g.id = (:idGameEntity)";
                session.createQuery(addGame)
                        .setParameter("newIdPurchase", Integer.parseInt(cartID))
                        .setParameter("idGameEntity", game.getId())
                        .executeUpdate();
                tx.commit();

                message = "Game successfully added to your cart ! :)";
            } else if(inputJson.get("action").equals("delete")) {
                System.out.println("HERE 1");
                System.out.println("PARAMS --> " + inputJson.toString());
                cartID = inputJson.get("cart").toString();
                System.out.println("HERE 4");
                String gameQuery = "from GameEntity g where g.idPurchase = (:idPurchase) AND g.idGame = (:idGame)";
                List games = session.createQuery(gameQuery)
                        .setParameter("idPurchase", Integer.parseInt(cartID))
                        .setParameter("idGame", Integer.parseInt(inputJson.get("game").toString()))
                        .getResultList();
                System.out.println("HERE 2");


                if (games == null || games.size() == 0) {
                    throw new Exception("Trying to delete a game that is not present in the cart");
                }

                GameEntity game = (GameEntity) games.get(0);

                System.out.println("HERE 3");
                Transaction tx = session.beginTransaction();
                String addGame = "UPDATE GameEntity g SET g.idPurchase = NULL " +
                        "WHERE g.id = (:idGameEntity)";
                session.createQuery(addGame)
                        .setParameter("idGameEntity", game.getId())
                        .executeUpdate();
                tx.commit();

                message = "Game successfully removed from your cart ! :)";
            } else {
                throw new Exception("Unknown action");
            }
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }

        return Response.status(200)
                .entity(Json.createObjectBuilder()
                    .add("cart", cartID)
                    .add("message", message)
                    .build().toString())
                .build();
    }
}
