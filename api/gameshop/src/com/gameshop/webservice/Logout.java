package com.gameshop.webservice;

import com.gameshop.model.Customer;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

@Secured
@Path("/logout")
public class Logout {
    // Path to game list
    @GET
    @Produces("application/json")
    @Path("/")
    public javax.ws.rs.core.Response logout(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            String token = authorization.substring("Bearer".length()).trim();
            AuthManager authManager = new AuthManager();
            String user = authManager.getSubject(token);

            String resultQuery = "from Customer c where c.username = :username";
            Customer customer = (Customer)session.createQuery(resultQuery)
                    .setParameter("username", user)
                    .uniqueResult();

            boolean res = ConnexionManager.deleteCustomer(customer);
            System.out.println("CUSTOMER IN LOGOUT--> " + ConnexionManager.getCustomers().toString());
            if (!res) {
                throw new Exception("Invalid customer, cannot logout");
            }
        } catch (Exception e) {
            return Response.status(500).entity("").build();
        }
        return Response.status(200).entity("").build();
    }

}
