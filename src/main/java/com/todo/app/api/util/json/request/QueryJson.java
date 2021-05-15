package com.todo.app.api.util.json.request;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiIgnore
public class QueryJson implements Serializable {

    @ApiModelProperty(value = "Database query", example = "SELECT * FROM users")
    @NotNull
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
