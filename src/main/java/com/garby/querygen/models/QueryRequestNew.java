package com.garby.querygen.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class QueryRequestNew {

    @Schema(description = "Whether the report should be aggregated or not")
    private boolean aggregateReport;

    @Schema(description = "List of relations for the query")
    private List<Relation> relations;

    @Schema(description = "List of aggregate functions")
    private List<AggregateFunction> aggregateFunctions;

    @Schema(description = "List of filters to apply")
    private List<String> filters;

    @Schema(description = "List of columns to order by", allowableValues = {"transaction.transDate", "customer.userId"})
    private List<String> orderBy;

    @Schema(description = "List of columns to group by")
    private List<String> groupBy;

    // Constructors, getters, and setters


    public QueryRequestNew() {
    }

    public QueryRequestNew(boolean aggregateReport, List<Relation> relations, List<AggregateFunction> aggregateFunctions, List<String> filters, List<String> orderBy, List<String> groupBy) {
        this.aggregateReport = aggregateReport;
        this.relations = relations;
        this.aggregateFunctions = aggregateFunctions;
        this.filters = filters;
        this.orderBy = orderBy;
        this.groupBy = groupBy;
    }

    public boolean isAggregateReport() {
        return aggregateReport;
    }

    public void setAggregateReport(boolean aggregateReport) {
        this.aggregateReport = aggregateReport;
    }

//    public RelationNew getRelations() {
//        return relations;
//    }

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
