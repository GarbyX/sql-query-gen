package com.garby.querygen.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.ArrayList;

public class QueryRequestNew {

    @Schema(description = "Whether the report should be aggregated or not")
    private boolean aggregateReport;

    @Schema(description = "List of relations for the query")
    private List<Relation> relations = new ArrayList<>(); // Initialize to an empty list

    @Schema(description = "List of aggregate functions")
    private List<AggregateFunction> aggregateFunctions;

    @Schema(description = "List of filters to apply")
    private List<String> filters;

    @Schema(description = "List of columns to order by", allowableValues = {"transaction.transDate", "customer.userId"})
    private List<String> orderBy;

    @Schema(description = "List of columns to group by")
    private List<String> groupBy;
    private RequestBody requestBody; // Add RequestBody field

    // Constructor
    public QueryRequestNew() {
        // Default constructor. Optionally initialize other fields
    }

    // Getters and Setters
    public boolean isAggregateReport() {
        return aggregateReport;
    }

    public void setAggregateReport(boolean aggregateReport) {
        this.aggregateReport = aggregateReport;
    }

    public List<Relation> getRelations() {
        return relations;    // This will return an empty list instead of null
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

    // Getter for RequestBody
    public RequestBody getRequestBody() {
        return requestBody;
    }

    // Setter for RequestBody
    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    // Nested static class for RequestBody structure
    public static class RequestBody {
        private boolean aggregateReport;
        private List<Relation> relations;
        private List<AggregateFunction> aggregateFunctions;
        private List<DefaultValueMapping> defaultValueMapping;

        // Getters and Setters for RequestBody

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

        public List<DefaultValueMapping> getDefaultValueMapping() {
            return defaultValueMapping;
        }

        public void setDefaultValueMapping(List<DefaultValueMapping> defaultValueMapping) {
            this.defaultValueMapping = defaultValueMapping;
        }
    }

    // DefaultValueMapping nested class
    public static class DefaultValueMapping {
        private Key key;
        private Value value;

        // Getters and Setters for DefaultValueMapping

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        // Nested classes for Key and Value

        public static class Key {
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

        public static class Value {
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


}
