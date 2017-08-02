package com.gameshop.webservice;

import org.hibernate.query.Query;

public class QueryPair {
    private Query query;
    private Query count;

    public QueryPair(Query query, Query count) {
        this.query = query;
        this.count = count;
    }

    public Query getQuery() {
        return query;
    }

    public Query getCount() {
        return count;
    }
}
