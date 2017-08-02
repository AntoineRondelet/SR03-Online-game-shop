package com.gameshop.webservice;

import javax.json.JsonObject;

public class PaginationInfo {
    private int httpCode;
    private JsonObject paginationField;

    public PaginationInfo(int httpCode, JsonObject paginationField) {
        this.httpCode = httpCode;
        this.paginationField = paginationField;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public JsonObject getPaginationField() {
        return paginationField;
    }

    static int calculateFirstResultIndex(int page, int itemsPerPage) {
        return (page - 1) * itemsPerPage;
    }
}
