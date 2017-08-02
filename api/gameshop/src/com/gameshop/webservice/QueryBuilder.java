package com.gameshop.webservice;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public class QueryBuilder {

    static QueryPair buildQueryPair(
            Session session, String baseQuery,
            Map<String, Object> params, List<String> subQueries,
            int page, int itemsPerPage
    ) {
        String queryString = baseQuery;
        if (subQueries.size() > 0) {
            queryString = String.join(" WHERE ", baseQuery, String.join( " AND ", subQueries));
        } else if (subQueries.size() == 1) {
            queryString = String.join(" WHERE ", baseQuery, subQueries.get(0));
        }

        // --- Prepare query to count entries in the table --- //
        String countQuery = "SELECT COUNT(*)" + queryString;
        Query count = session.createQuery(countQuery);

        Query query = session.createQuery(queryString)
                .setMaxResults(itemsPerPage)
                .setFirstResult(PaginationInfo.calculateFirstResultIndex(page, itemsPerPage));

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            count.setParameter(entry.getKey(), entry.getValue());
        }

        return new QueryPair(query, count);
    }
}
