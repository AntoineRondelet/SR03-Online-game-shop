package com.gameshop.webservice;

import com.gameshop.model.Purchase;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.json.Json;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

@Secured
@Path("/cart")
public class CartPath {
    @GET
    @Transactional
    @Produces("application/json")
    @Path("/validate/{id}")
    public javax.ws.rs.core.Response validatePurchase(
            @PathParam("id") int idPurchase
    ){
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            Transaction tx = session.beginTransaction();
            String gameQuery = "UPDATE Purchase p  SET p.paymentStatus = (:newStatus) where p.id = (:idPurchase)";
            session.createQuery(gameQuery)
                    .setParameter("newStatus", "Done")
                    .setParameter("idPurchase", idPurchase)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }

        return Response.status(200)
                .entity(Json.createObjectBuilder()
                        .add("message", "Thank you for you order. We hope to have yu back soon on gameshop.com")
                        .build().toString())
                .build();
    }

    @GET
    @Transactional
    @Produces("application/json")
    @Path("/drop/{id}")
    public javax.ws.rs.core.Response dropPurchase(
            @PathParam("id") int idPurchase
    ){
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            System.out.println("DROP 1");
            String verifyCart = "FROM Purchase p WHERE p.id = (:idPurchase)";
            Purchase purchase = (Purchase) session.createQuery(verifyCart)
                    .setParameter("idPurchase", idPurchase)
                    .uniqueResult();

            if (purchase == null || purchase.getPaymentStatus() == "Done") {
                return Response.status(200).entity("").build();
            }

            System.out.println("DROP 2");

            // Delete all the game entities that belong to the purchase
            Transaction tx1 = session.beginTransaction();
            String deleteGame = "UPDATE GameEntity g SET g.idPurchase = NULL " +
                    "WHERE g.idPurchase = (:idPurchase)";
            session.createQuery(deleteGame)
                    .setParameter("idPurchase", idPurchase)
                    .executeUpdate();
            tx1.commit();

            System.out.println("DROP 3");
            // Delete the purchase from the table of purchase
            Transaction tx2 = session.beginTransaction();
            String gameQuery = "DELETE FROM Purchase p WHERE p.id = (:idPurchase)";
            session.createQuery(gameQuery)
                    .setParameter("idPurchase", idPurchase)
                    .executeUpdate();
            tx2.commit();
            System.out.println("DROP 4");
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }

        return Response.status(200).entity("").build();
    }
}
