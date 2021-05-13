package com.todo.app.api.controller.util.json;

import java.io.Serializable;

public class QueryJson implements Serializable {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
