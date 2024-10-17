package com.garby.querygen.models;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Schema(description = "Relation information for joining tables")
public class Relation {
    private String table;
    private List<String> fields;
    private String joinType;

    // Constructor
    public Relation(String table, List<String> fields, String joinType) {
        this.table = table;
        this.fields = fields;
        this.joinType = joinType;
    }

    // Getters and Setters

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }
}
