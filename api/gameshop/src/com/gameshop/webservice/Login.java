package com.gameshop.webservice;

import com.gameshop.model.Customer;
import com.gameshop.model.Game;
import org.hibernate.Session;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

// -- This endpoint is not secure and can be reach by anyone -- //

@Path("/login")
public class Login {
    @POST
    @Produces("application/json")
    @Path("/")
    public javax.ws.rs.core.Response login(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {
        // --- DB management --- //
        Session session = getSessionFactory().openSession();

        System.out.println("DEBUG 2");
        // -- Check that the user that is trying to access the website is logged in -- //
        // TODO: Crypter les mdp en BDD et comparer les string des mdp chiffrés
        String resultQuery = "from Customer c where c.username = :username and c.password = :password";
        Customer customer = (Customer)session.createQuery(resultQuery)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();

        if (customer == null) {
            // -- Return a unauthorized (401) HTTP response -- //
            System.out.println("DEBUG 1");
            return Response.status(Response.Status.UNAUTHORIZED).entity("").build();
        }

        System.out.println("DEBUG 3");
        // -- Add the customer in the list of connected customers -- //
        if (!ConnexionManager.isConnected(customer)) {
            ConnexionManager.addCustomers(customer);
        }
        System.out.println("CUSTOMER LOGIN --> " + ConnexionManager.getCustomers());

        // -- Generate a token for the user -- //
        AuthManager auth = new AuthManager();
        String token = auth.createJWT(username);

        System.out.println("DEBUG 4 --> " + token);
        JsonObjectBuilder jsonResult = Json.createObjectBuilder();
        jsonResult.add("redirect_url", "http://www.gameshop.com")
                .add("token", token);
        return Response.ok().entity(jsonResult.build().toString()).build();
    }
}
