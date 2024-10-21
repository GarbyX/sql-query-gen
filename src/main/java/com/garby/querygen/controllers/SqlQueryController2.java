package com.garby.querygen.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garby.querygen.models.DefaultValueMappingEntity;
import com.garby.querygen.models.QueryRequestNew;
import com.garby.querygen.models.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SqlQueryController2 {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryController2.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper instance

    // Endpoint for handling query request
    @PostMapping("/api/v1/sqlquery2")
    public ResponseEntity<?> handleQuery2(@RequestBody QueryRequestNew queryRequestNew) {
        try {
            // Convert the request object to JSON string for logging
            String jsonPayload = objectMapper.writeValueAsString(queryRequestNew);
            logger.info("Received request: {}", jsonPayload); // Log the JSON payload

            // Validate relations
            if (queryRequestNew.getRelations() == null ||
                    queryRequestNew.getRelations().isEmpty()) {
                throw new IllegalArgumentException("Relations cannot be null or empty");
            }

            // Handle default value mapping and store them separately
            List<DefaultValueMappingEntity> defaultValueMappings = handleDefaultValueMapping(queryRequestNew.getRequestBody());

            // Build the SQL query
            StringBuilder sqlQuery = new StringBuilder();
            sqlQuery.append(buildSqlQuery(queryRequestNew));

            // Add the default value mappings to SQL query
            sqlQuery = new StringBuilder(buildSqlWithDefaultMappings(sqlQuery, defaultValueMappings));

            return ResponseEntity.ok(sqlQuery.toString()); // Return the constructed SQL query
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request format");
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Method for handling defaultValueMapping section from the payload
    private List<DefaultValueMappingEntity> handleDefaultValueMapping(QueryRequestNew.RequestBody body) {
        List<DefaultValueMappingEntity> defaultValueMappings = new ArrayList<>();

        // Check if defaultValueMapping is present in the request
        if (body.getDefaultValueMapping() != null && !body.getDefaultValueMapping().isEmpty()) {
            for (QueryRequestNew.DefaultValueMapping defaultValue : body.getDefaultValueMapping()) {
                DefaultValueMappingEntity mappingEntity = new DefaultValueMappingEntity();

                // Set values in the entity from the payload
                mappingEntity.setSourceTable(defaultValue.getKey().getDftable());     // Key table
                mappingEntity.setSourceField(defaultValue.getKey().getDffield());     // Key field
                mappingEntity.setTargetTable(defaultValue.getValue().getDftable());   // Value table
                mappingEntity.setTargetField(defaultValue.getValue().getDffield());   // Value field

                // Add the entity to the list
                defaultValueMappings.add(mappingEntity);
            }
        }
        return defaultValueMappings; // Return the list of default mappings
    }

    // Build the SQL query based on the request payload
    private String buildSqlQuery(QueryRequestNew queryRequest) {
        StringBuilder sql = new StringBuilder();

        // Start the SELECT statement
        sql.append("SELECT ");

        // Append fields from relations
        boolean firstField = true;
        for (Relation relation : queryRequest.getRelations()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append(String.join(", ", relation.getFields()));
            firstField = false;
        }

        sql.append(" FROM ").append(queryRequest.getRelations().get(0).getTable()); // Use the first relation's table as the main table

        // Handle joins based on the relations array
        for (int i = 1; i < queryRequest.getRelations().size(); i++) {
            Relation relation = queryRequest.getRelations().get(i);
            sql.append(" ").append(relation.getJoinType()).append(" JOIN ").append(relation.getTable())
               .append(" ON ").append(queryRequest.getRelations().get(0).getTable()).append(".userId = ")
               .append(relation.getTable()).append(".userId ");
        }

        // Append WHERE clause if filters exist
        if (queryRequest.getFilters() != null && !queryRequest.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", queryRequest.getFilters()));
        }

        // Append ORDER BY clause
        if (queryRequest.getOrderBy() != null && !queryRequest.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", queryRequest.getOrderBy()));
        }

        // Append GROUP BY clause if aggregateReport is TRUE
        if (queryRequest.getGroupBy() != null && !queryRequest.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", queryRequest.getGroupBy()));
        }

        return sql.toString(); // Return the constructed SQL query
    }

    // Build SQL query for default value mappings
    private String buildSqlWithDefaultMappings(StringBuilder sql, List<DefaultValueMappingEntity> defaultValueMappings) {
        if (!defaultValueMappings.isEmpty()) {
            for (DefaultValueMappingEntity mappingEntity : defaultValueMappings) {
                sql.append(" LEFT JOIN ").append(mappingEntity.getTargetTable())   // Join with the target table
                   .append(" ON ").append(mappingEntity.getSourceTable())  // Source table
                   .append(".").append(mappingEntity.getSourceField())  // Source field
                   .append(" = ").append(mappingEntity.getTargetTable())  // Target table
                   .append(".").append(mappingEntity.getTargetField()).append(" ");  // Target field
            }
        }
        return sql.toString();
    }
}
