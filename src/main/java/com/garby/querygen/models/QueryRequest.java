package com.garby.querygen.models;

import java.util.ArrayList;
import java.util.List;

public class QueryRequest {
    private boolean aggregateReport;
    private List<Relation> relations = new ArrayList<>(); // Initialize to an empty list;
    private List<AggregateFunction> aggregateFunctions;
    private List<String> filters;
    private List<String> orderBy;
    private List<String> groupBy;

    // Constructor
    public QueryRequest() {
        // Default constructor
    }

    // Getters and Setters

    public boolean isAggregateReport() {
        return aggregateReport;
    }

    public void setAggregateReport(boolean aggregateReport) {
        this.aggregateReport = aggregateReport;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<AggregateFunction> getAggregateFunctions() {
        return aggregateFunctions;
    }

    public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }
}

