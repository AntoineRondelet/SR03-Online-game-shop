package com.gameshop.webservice;

import org.hibernate.query.Query;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class PaginationManager {
    private int page;
    private int itemsPerPage;

    public PaginationManager(int page, int itemsPerPage) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
    }

    public PaginationInfo buildPaginationInfo(Query countQuery) {
        int pageNumber;
        Long totalHits = (Long) countQuery.uniqueResult();
        if (totalHits % this.itemsPerPage == (long) 0) {
            pageNumber = (int) (totalHits / this.itemsPerPage);
        } else {
            pageNumber = (int) ((totalHits / this.itemsPerPage) + 1);
        }

        boolean isLast = this.page >= pageNumber;

        // --- JSON object containing pagination information --- //
        JsonObjectBuilder paginationField = Json.createObjectBuilder()
                .add("current_page", this.page)
                .add("items_per_page", this.itemsPerPage)
                .add("is_last", isLast)
                .add("total_hits", totalHits);

        if (isLast) {
            paginationField.add("pages", Json.createObjectBuilder()
                    .add("next", pageNumber)
                    .add("last", pageNumber));

            return new PaginationInfo(200, paginationField.build());
        }

        paginationField.add("pages", Json.createObjectBuilder()
            .add("next", this.page + 1)
            .add("last", pageNumber));

        return new PaginationInfo(206, paginationField.build());
    }
}
