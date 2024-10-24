package com.garby.querygen.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Relation information for joining tables")
public class Relation {
    @Schema(description = "Table name for the relation", allowableValues = {"customer", "transaction"})
    private String table;
    @Schema(description = "Fields to be selected from the table", allowableValues = {"userId", "username", "DOB", "transDate", "amount", "reference", "status", "transactionUser"})
    private List<String> fields;
    @Schema(description = "Type of join for the relation", allowableValues = {"left join", "inner join"})
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
