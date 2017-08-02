package com.gameshop.webservice;

import com.gameshop.model.Customer;
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
@Path("/customers")
public class CustomerPath {
    @GET
    @Produces("application/json")
    @Path("/")
    // --- This function returns the list of all customer in the store --- //
    public javax.ws.rs.core.Response getCustomers(
            @DefaultValue("10") @QueryParam("itemsPerPage") int itemsPerPage,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("-1") @QueryParam("isActive") int isActive
    ) {
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Build query dynamically according to parameters values --- //
            String baseQuery = "FROM Customer c";
            Map<String, Object> params = new HashMap<>();
            List<String> subQueries = new ArrayList<>();
            if (isActive != -1) {
                subQueries.add("c.isactive = :isActive");
                if (isActive == 0) {
                    params.put("isActive", false);
                } else {
                    params.put("isActive", true);
                }
            }

            // --- Build queries --- //
            QueryPair queries = QueryBuilder.buildQueryPair(session, baseQuery, params, subQueries, page, itemsPerPage);

            // --- Get pagination information --- //
            PaginationInfo paginationInfo = new PaginationManager (page, itemsPerPage)
                    .buildPaginationInfo(queries.getCount());

            // --- Get result list --- //
            List customer = queries.getQuery().getResultList();

            // --- Closing connection to DB --- //
            session.close();

            // --- Return HTTP Response --- //
            return Response.status(paginationInfo.getHttpCode())
                    .entity(buildCustomerListResponse(customer, paginationInfo.getPaginationField()))
                    .build();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }
    }

    private String buildCustomerListResponse(List customers, JsonObject paginationField) {
        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        // --- JSON Array of all the games --- //
        JsonArrayBuilder jsonCustomerList = Json.createArrayBuilder();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = (Customer) customers.get(i);
            jsonCustomerList.add(Json.createObjectBuilder()
                .add("username", customer.getUsername())
                .add("first name", customer.getFirstName())
                .add("last name", customer.getLastName())
                .add("birth date", customer.getBirthDate().toString())
                .add("subscription date", customer.getSubscriptionDate().toString())
                .add("active", customer.isIsactive())
                .add("url", Json.createObjectBuilder()
                    .add("path", "/customers/"+ customer.getUsername())));
        }

        // --- Add the JSON Array to the JSON response --- //
        return jsonResponse.add("list", jsonCustomerList.build())
                .add("pagination", paginationField)
                .build().toString();
    }

    @GET
    @Produces("application/json")
    @Path("/{username}")
    // --- This function returns the characteristics of a given game (specific resource) --- //
    public String getCustomer(
            @PathParam("username") String username
    ) {
        // --- DB management --- //
        Session session = getSessionFactory().openSession();

        // --- Query the given customer's characteristics --- //
        String resultQuery = "from Customer c where c.username = :username";
        Customer customer = (Customer)session.createQuery(resultQuery)
                .setParameter("username", username)
                .uniqueResult();

        // --- Building response body --- //
        JsonObjectBuilder jsonResult = Json.createObjectBuilder();
        jsonResult.add("username", customer.getUsername())
                .add("first_name", customer.getFirstName())
                .add("last_name", customer.getLastName())
                .add("email", customer.getEmail())
                .add("is_active", customer.isIsactive())
                .add("birth_date", customer.getBirthDate().toString())
                .add("subscription_date", customer.getSubscriptionDate().toString());

        return jsonResult.build().toString();
    }
}
