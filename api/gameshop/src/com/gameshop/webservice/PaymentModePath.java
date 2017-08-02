package com.gameshop.webservice;

import com.gameshop.model.PaymentMode;
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
@Path("/paymentModes")
public class PaymentModePath {
    @GET
    @Produces("application/json")
    @Path("/")
    // --- This function returns the list of all payment modes supported by the store --- //
    public javax.ws.rs.core.Response getPaymentModes(
            @DefaultValue("10") @QueryParam("itemsPerPage") int itemsPerPage,
            @DefaultValue("1") @QueryParam("page") int page
    ){
        try {
            // --- DB management --- //
            Session session = getSessionFactory().openSession();

            // --- Build query dynamically according to parameters values --- //
            String baseQuery = "FROM PaymentMode pm";
            Map<String, Object> params = new HashMap<>();
            List<String> subQueries = new ArrayList<>();

            // --- Build queries --- //
            QueryPair queries = QueryBuilder.buildQueryPair(session, baseQuery, params, subQueries, page, itemsPerPage);

            // --- Get pagination information --- //
            PaginationInfo paginationInfo = new PaginationManager (page, itemsPerPage)
                    .buildPaginationInfo(queries.getCount());

            // --- Get result list --- //
            List paymentModes = queries.getQuery().getResultList();

            // --- Closing connection to DB --- //
            session.close();

            // --- Return HTTP Response --- //
            return Response.status(paginationInfo.getHttpCode())
                    .entity(buildPaymentModeListResponse(paymentModes, paginationInfo.getPaginationField()))
                    .build();
        } catch (Exception e) {
            // --- Internal Error --- //
            System.err.println("Internal error handled " + e);
            return Response.status(500).entity("").build();
        }
    }

    private String buildPaymentModeListResponse(List paymentModes, JsonObject paginationField) {
        JsonObjectBuilder jsonResponse = Json.createObjectBuilder();

        // --- JSON Array of all the games --- //
        JsonArrayBuilder jsonConsoleList = Json.createArrayBuilder();
        for (int i = 0; i < paymentModes.size(); i++) {
            PaymentMode paymentMode = (PaymentMode) paymentModes.get(i);
            jsonConsoleList.add(Json.createObjectBuilder()
                    .add("mode", paymentMode.getPaymentMode()));
        }

        // --- Add the JSON Array to the JSON response --- //
        return jsonResponse.add("list", jsonConsoleList.build())
                .add("pagination", paginationField)
                .build().toString();
    }
}
