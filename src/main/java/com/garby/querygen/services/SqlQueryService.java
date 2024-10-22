package com.garby.querygen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.garby.querygen.models.DefaultValueMappingEntity;
import com.garby.querygen.models.QueryRequestNew;
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

}

