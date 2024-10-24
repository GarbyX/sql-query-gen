package com.garby.querygen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.garby.querygen.models.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class SqlQueryService {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public String processQuery(QueryRequestNew queryRequest) throws JsonProcessingException {
        // Convert the request object to formatted JSON string for logging
        String jsonPayload = objectMapper.writeValueAsString(queryRequest);
        logRequest(jsonPayload);

        QueryRequestNew.RequestBody requestBody = queryRequest.getRequestBody();

        // Validate relations
        if (requestBody.getRelations() == null || requestBody.getRelations().isEmpty()) {
            throw new IllegalArgumentException("Relations cannot be null or empty");
        }

        // Handle default value mapping and store them separately
        List<DefaultValueMappingEntity> defaultValueMappings = handleDefaultValueMapping(requestBody);

        // Build the SQL query
        StringBuilder sqlQuery = new StringBuilder(buildSqlQuery(requestBody));

        // Add the default value mappings to SQL query
        sqlQuery = new StringBuilder(buildSqlWithDefaultMappings(sqlQuery, defaultValueMappings));

        // Log the formatted output SQL query
        logOutput(sqlQuery.toString());

        return sqlQuery.toString();
    }

    private List<DefaultValueMappingEntity> handleDefaultValueMapping(QueryRequestNew.RequestBody body) {
        List<DefaultValueMappingEntity> defaultValueMappings = new ArrayList<>();

        if (body.getDefaultValueMapping() != null && !body.getDefaultValueMapping().isEmpty()) {
            for (QueryRequestNew.DefaultValueMapping defaultValue : body.getDefaultValueMapping()) {
                DefaultValueMappingEntity mappingEntity = new DefaultValueMappingEntity();
                mappingEntity.setSourceTable(defaultValue.getKey().getDftable());
                mappingEntity.setSourceField(defaultValue.getKey().getDffield());
                mappingEntity.setTargetTable(defaultValue.getValue().getDftable());
                mappingEntity.setTargetField(defaultValue.getValue().getDffield());
                defaultValueMappings.add(mappingEntity);
            }
        }
        return defaultValueMappings;
    }

    private String buildSqlQuery(QueryRequestNew.RequestBody requestBody) {
        StringBuilder sql = new StringBuilder();

        // Start SELECT clause
        sql.append("SELECT ");

        boolean firstField = true;
        for (QueryRequestNew.Relation relation : requestBody.getRelations()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append(String.join(", ", relation.getFields()));
            firstField = false;
        }

        // FROM clause with join
        sql.append(" FROM ").append(requestBody.getRelations().get(0).getTable());

        for (int i = 1; i < requestBody.getRelations().size(); i++) {
            QueryRequestNew.Relation relation = requestBody.getRelations().get(i);
            if (!relation.getJoinType().isEmpty()) {
                sql.append(" ").append(relation.getJoinType()).append(" JOIN ").append(relation.getTable())
                        .append(" ON ").append(requestBody.getRelations().get(0).getTable()).append(".userId = ")
                        .append(relation.getTable()).append(".userId ");
            }
        }

        // WHERE clause
        if (requestBody.getFilters() != null && !requestBody.getFilters().isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", requestBody.getFilters()));
        }

        // ORDER BY clause
        if (requestBody.getOrderBy() != null && !requestBody.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", requestBody.getOrderBy()));
        }

        // GROUP BY clause
        if (requestBody.getGroupBy() != null && !requestBody.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", requestBody.getGroupBy()));
        }

        return sql.toString();
    }

    private String buildSqlWithDefaultMappings(StringBuilder sql, List<DefaultValueMappingEntity> defaultValueMappings) {
        if (!defaultValueMappings.isEmpty()) {
            for (DefaultValueMappingEntity mappingEntity : defaultValueMappings) {
                sql.append(" LEFT JOIN ").append(mappingEntity.getTargetTable())
                        .append(" ON ").append(mappingEntity.getSourceTable()).append(".")
                        .append(mappingEntity.getSourceField()).append(" = ")
                        .append(mappingEntity.getTargetTable()).append(".")
                        .append(mappingEntity.getTargetField());
            }
        }
        return sql.toString();
    }

    // Log the request in readable format
    private void logRequest(String jsonPayload) {
        Logger logger = LoggerFactory.getLogger(SqlQueryService.class);
        logger.info("Received request:\n{}", jsonPayload);
    }

    // Log the output SQL query in readable format
    private void logOutput(String sqlQuery) {
        Logger logger = LoggerFactory.getLogger(SqlQueryService.class);
        logger.info("Generated SQL query:\n{}", sqlQuery);
    }

    // -------------- ported code -------------- //

    public String generateSQLFromRequest(QueryRequest request) {
        // Base SQL Query
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ");
        // Handle SELECT fields
        // Statement lambda replaced with expression lambda
        request.getRelations().forEach(relation -> relation.getFields().forEach(field -> queryBuilder.append(relation.getTable()).append(".").append(field).append(", ")));
        // Remove last comma and space
        queryBuilder.setLength(queryBuilder.length() - 2);
        // Handle FROM and JOIN clauses
        queryBuilder.append(" FROM ").append(request.getRelations().get(0).getTable()).append(" ");
        for (int i = 1; i < request.getRelations().size(); i++) {
            Relation relation = request.getRelations().get(i);
            queryBuilder.append(relation.getJoinType().toUpperCase())
                    .append(" ")
                    .append(relation.getTable())
                    .append(" ON ")
                    .append(request.getRelations().get(0).getTable())
                    .append(".userId = ")
                    .append(relation.getTable())
                    .append(".userId ");
        }

        // Handle WHERE clause
        queryBuilder.append("WHERE ");
        request.getFilters().forEach(filter -> queryBuilder.append(filter).append(" IS NOT NULL AND "));
        // Remove last 'AND'
        queryBuilder.setLength(queryBuilder.length() - 4);
        // Handle ORDER BY clause
        queryBuilder.append(" ORDER BY ");
        request.getOrderBy().forEach(orderField -> queryBuilder.append(orderField).append(", "));
        // Remove last comma
        queryBuilder.setLength(queryBuilder.length() - 2);
        return queryBuilder.toString();
    }



    public String generateSqlQuery(QueryRequestNew queryRequest) {
        StringBuilder sql = new StringBuilder();
        boolean aggregateReport = queryRequest.getRequestBody().isAggregateReport();

        // Start the SELECT statement
        sql.append("SELECT ");

        // Handle fields for SELECT
        boolean firstField = true;
        for (QueryRequestNew.Relation relation : queryRequest.getRequestBody().getRelations()) {
            for (String field : relation.getFields()) {
                if (!firstField) {
                    sql.append(", ");
                }
                sql.append(field);
                firstField = false;
            }
        }

        // Add the FROM clause (main table)
        sql.append(" FROM ").append(queryRequest.getRequestBody().getRelations().get(0).getTable());

        // Handle JOINs based on relations
        for (int i = 1; i < queryRequest.getRequestBody().getRelations().size(); i++) {
            QueryRequestNew.Relation relation = queryRequest.getRequestBody().getRelations().get(i);
            if (relation.getJoinType() != null && !relation.getJoinType().isEmpty()) {
                sql.append(" ").append(relation.getJoinType()).append(" JOIN ")
                        .append(relation.getTable()).append(" ON ecardb.userdb = ").append(relation.getTable()).append(".user");
            }
        }

        // Add WHERE clause if there are filters
        if (queryRequest.getRequestBody().getFilters() != null && !queryRequest.getRequestBody().getFilters().isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", queryRequest.getRequestBody().getFilters()));
        }

        // Add GROUP BY clause (only if aggregateReport is true)
        if (aggregateReport && queryRequest.getRequestBody().getGroupBy() != null && !queryRequest.getRequestBody().getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", queryRequest.getRequestBody().getGroupBy()));
        }

        // Add ORDER BY clause
        if (queryRequest.getRequestBody().getOrderBy() != null && !queryRequest.getRequestBody().getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", queryRequest.getRequestBody().getOrderBy()));
        }

        sql.append(";");  // End the query with a semicolon
        return sql.toString();
    }


    // Todo: use this validation
    public void validatePayload(QueryRequestNew queryRequest) {
        if (queryRequest.getRequestBody().getRelations() == null || queryRequest.getRequestBody().getRelations().isEmpty()) {
            throw new IllegalArgumentException("Relations cannot be null or empty");
        }

        for (QueryRequestNew.Relation relation : queryRequest.getRequestBody().getRelations()) {
            if (relation.getTable() == null || relation.getFields() == null || relation.getFields().isEmpty()) {
                throw new IllegalArgumentException("Each relation must have a table and at least one field");
            }
        }

        if (queryRequest.getRequestBody().isAggregateReport() && (queryRequest.getRequestBody().getGroupBy() == null || queryRequest.getRequestBody().getGroupBy().isEmpty())) {
            throw new IllegalArgumentException("GroupBy is required when aggregateReport is true");
        }
    }



}

