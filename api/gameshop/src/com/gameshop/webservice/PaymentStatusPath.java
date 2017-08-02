package com.gameshop.webservice;

import com.gameshop.model.PaymentStatus;
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
@Path("/paymentStatus")
public class PaymentStatusPath {
    @GET
    @Produces("application/json")
    @Path("/")
    // --- This function returns the list of all payment statuses supported by the store --- //
    public javax.ws.rs.core.Response getPaymentStatus(
            @DefaultValue("10") @QueryParam("itemsPerPage") int itemsPerPage,
            @DefaultValue("1") @QueryParam("page") int page
    ){
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Build query dynamically according to parameters values --- //
            String baseQuery = "FROM PaymentStatus ps";
            Map<String, Object> params = new HashMap<>();
            List<String> subQueries = new ArrayList<>();

            // --- Build queries --- //
            QueryPair queries = QueryBuilder.buildQueryPair(session, baseQuery, params, subQueries, page, itemsPerPage);

            // --- Get pagination information --- //
            PaginationInfo paginationInfo = new PaginationManager (page, itemsPerPage)
                    .buildPaginationInfo(queries.getCount());

            // --- Get result list --- //
            List paymentStatus = queries.getQuery().getResultList();

            // --- Closing connection to DB --- //
            session.close();

            // --- Return HTTP Response --- //
            return Response.status(paginationInfo.getHttpCode())
                    .entity(buildPaymentStatusListResponse(paymentStatus, paginationInfo.getPaginationField()))
                    .build();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }
    }

    private String buildPaymentStatusListResponse(List paymentStatusList, JsonObject paginationField) {
        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        // --- JSON Array of all the games --- //
        JsonArrayBuilder jsonConsoleList = Json.createArrayBuilder();
        for (int i = 0; i < paymentStatusList.size(); i++) {
            PaymentStatus paymentStatus = (PaymentStatus) paymentStatusList.get(i);
            jsonConsoleList.add(Json.createObjectBuilder()
                    .add("mode", paymentStatus.getPaymentStatus()));
        }

        // --- Add the JSON Array to the JSON response --- //
        return jsonResponse.add("list", jsonConsoleList.build())
                .add("pagination", paginationField)
                .build().toString();
    }
}

