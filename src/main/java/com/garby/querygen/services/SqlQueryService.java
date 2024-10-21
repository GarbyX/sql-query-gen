package com.garby.querygen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garby.querygen.models.DefaultValueMappingEntity;
import com.garby.querygen.models.QueryRequestNew;
import com.garby.querygen.models.Relation;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class SqlQueryService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to process the QueryRequest and generate SQL query
    public String processQuery(QueryRequestNew queryRequestNew) throws JsonProcessingException, JsonProcessingException {
        // Convert the request object to JSON string for logging
        String jsonPayload = objectMapper.writeValueAsString(queryRequestNew);
        logRequest(jsonPayload);

        // Validate relations
        if (queryRequestNew.getRelations() == null || queryRequestNew.getRelations().isEmpty()) {
            throw new IllegalArgumentException("Relations cannot be null or empty");
        }

        // Handle default value mapping and store them separately
        List<DefaultValueMappingEntity> defaultValueMappings = handleDefaultValueMapping(queryRequestNew.getRequestBody());

        // Build the SQL query
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(buildSqlQuery(queryRequestNew));

        // Add the default value mappings to SQL query
        sqlQuery = new StringBuilder(buildSqlWithDefaultMappings(sqlQuery, defaultValueMappings));

        return sqlQuery.toString();
    }

    // Method for handling defaultValueMapping section from the payload
    private List<DefaultValueMappingEntity> handleDefaultValueMapping(QueryRequestNew.RequestBody body) {
        List<DefaultValueMappingEntity> defaultValueMappings = new ArrayList<>();

        // Check if defaultValueMapping is present in the request
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

    // Build the SQL query based on the request payload
    private String buildSqlQuery(QueryRequestNew queryRequestNew) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        boolean firstField = true;
        for (Relation relation : queryRequestNew.getRelations()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append(String.join(", ", relation.getFields()));
            firstField = false;
        }

        sql.append(" FROM ").append(queryRequestNew.getRelations().get(0).getTable());

        for (int i = 1; i < queryRequestNew.getRelations().size(); i++) {
            Relation relation = queryRequestNew.getRelations().get(i);
            sql.append(" ").append(relation.getJoinType()).append(" JOIN ").append(relation.getTable())
                    .append(" ON ").append(queryRequestNew.getRelations().get(0).getTable()).append(".userId = ")
                    .append(relation.getTable()).append(".userId ");
        }

        if (queryRequestNew.getFilters() != null && !queryRequestNew.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", queryRequestNew.getFilters()));
        }

        if (queryRequestNew.getOrderBy() != null && !queryRequestNew.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", queryRequestNew.getOrderBy()));
        }

        if (queryRequestNew.getGroupBy() != null && !queryRequestNew.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", queryRequestNew.getGroupBy()));
        }

        return sql.toString();
    }

    // Build SQL query for default value mappings
    private String buildSqlWithDefaultMappings(StringBuilder sql, List<DefaultValueMappingEntity> defaultValueMappings) {
        if (!defaultValueMappings.isEmpty()) {
            for (DefaultValueMappingEntity mappingEntity : defaultValueMappings) {
                sql.append(" LEFT JOIN ").append(mappingEntity.getTargetTable())
                        .append(" ON ").append(mappingEntity.getSourceTable()).append(".")
                        .append(mappingEntity.getSourceField()).append(" = ")
                        .append(mappingEntity.getTargetTable()).append(".")
                        .append(mappingEntity.getTargetField()).append(" ");
            }
        }
        return sql.toString();
    }

    // Log the JSON payload
    private void logRequest(String jsonPayload) {
        Logger logger = LoggerFactory.getLogger(SqlQueryService.class);
        logger.info("Received request: {}", jsonPayload);
    }
}

