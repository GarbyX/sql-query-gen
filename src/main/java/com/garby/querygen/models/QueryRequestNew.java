package com.garby.querygen.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.ArrayList;

public class QueryRequestNew {

//    @Schema(description = "Whether the report should be aggregated or not")
//    private boolean aggregateReport;
//
//    @Schema(description = "List of relations for the query")
//    private List<Relation> relations = new ArrayList<>(); // Initialize to an empty list
//
//    @Schema(description = "List of aggregate functions")
//    private List<AggregateFunction> aggregateFunctions;
//
//    @Schema(description = "List of filters to apply")
//    private List<String> filters;
//
//    @Schema(description = "List of columns to order by", allowableValues = {"transaction.transDate", "customer.userId"})
//    private List<String> orderBy;
//
//    @Schema(description = "List of columns to group by")
//    private List<String> groupBy;

    // private RequestBody requestBody; // Add RequestBody field

    private RequestBody requestBody;   // model class QueryRequest now includes a requestBody field and properly handles nested objects like defaultValueMapping.

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public static class RequestBody {
        @Schema(description = "Whether the report should be aggregated or not")
        private boolean aggregateReport;
        @Schema(description = "List of relations for the query")
        private List<Relation> relations = new ArrayList<>(); // Initialize to an empty list;
        @Schema(description = "List of aggregate functions")
        private List<AggregateFunction> aggregateFunctions;
        @Schema(description = "List of filters to apply")
        private List<String> filters;
        @Schema(description = "List of columns to order by", allowableValues = {"transaction.transDate", "customer.userId"})
        private List<String> orderBy;
        @Schema(description = "List of columns to group by")
        private List<String> groupBy;
        private List<DefaultValueMapping> defaultValueMapping;

        // Constructor
        // public QueryRequest() {
        // Default constructor. Optionally initialize other fields
    // }

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

    public List<DefaultValueMapping> getDefaultValueMapping() {
        return defaultValueMapping;
    }

    public void setDefaultValueMapping(List<DefaultValueMapping> defaultValueMapping) {
        this.defaultValueMapping = defaultValueMapping;
    }
}

public static class Relation {
    private String table;
    private List<String> fields;
    private String joinType;

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

public static class AggregateFunction {
    private String agfield;
    private String function;

    // Getters and Setters
    public String getAgfield() {
        return agfield;
    }

    public void setAgfield(String agfield) {
        this.agfield = agfield;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}

public static class DefaultValueMapping {
    private MappingKey key;
    private MappingValue value;

    // Getters and Setters
    public MappingKey getKey() {
        return key;
    }

    public void setKey(MappingKey key) {
        this.key = key;
    }

    public MappingValue getValue() {
        return value;
    }

    public void setValue(MappingValue value) {
        this.value = value;
    }
}

public static class MappingKey {
    private String dftable;
    private String dffield;

    // Getters and Setters
    public String getDftable() {
        return dftable;
    }

    public void setDftable(String dftable) {
        this.dftable = dftable;
    }

    public String getDffield() {
        return dffield;
    }

    public void setDffield(String dffield) {
        this.dffield = dffield;
    }
}

public static class MappingValue {
    private String dftable;
    private String dffield;

    // Getters and Setters
    public String getDftable() {
        return dftable;
    }

    public void setDftable(String dftable) {
        this.dftable = dftable;
    }

    public String getDffield() {
        return dffield;
    }

    public void setDffield(String dffield) {
        this.dffield = dffield;
    }
}

}
