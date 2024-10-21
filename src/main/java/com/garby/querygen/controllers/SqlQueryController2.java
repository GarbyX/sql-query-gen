package com.garby.querygen.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.garby.querygen.models.DefaultValueMappingEntity;
import com.garby.querygen.models.QueryRequestNew;
import com.garby.querygen.models.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class SqlQueryController2 {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryController2.class);
    private final ObjectMapper objectMapper;

    public SqlQueryController2() {
        // Enable pretty printing for ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostMapping("/api/v1/sqlquery2")
    public ResponseEntity<?> handleQuery2(@RequestBody QueryRequestNew queryRequestNew) {
        try {
            // Pretty-print the JSON request payload for logging
            String jsonPayload = objectMapper.writeValueAsString(queryRequestNew);
            logger.info("Received request: \n{}", jsonPayload); // Log the formatted JSON payload

            // Validate relations
            if (queryRequestNew.getRelations() == null || queryRequestNew.getRelations().isEmpty()) {
                throw new IllegalArgumentException("Relations cannot be null or empty");
            }

            // Build the SQL query
            StringBuilder sqlQuery = new StringBuilder(buildSqlQuery(queryRequestNew));

            // Log the SQL query in a readable format
            logger.info("Generated SQL Query: \n{}", formatSql(sqlQuery.toString())); // Format and log the SQL

            return ResponseEntity.ok(sqlQuery.toString()); // Return the constructed SQL query
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request format");
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Build the SQL query based on the request payload
    private String buildSqlQuery(QueryRequestNew queryRequestNew) {
        StringBuilder sql = new StringBuilder();

        // Start the SELECT statement
        sql.append("SELECT ");

        // Append fields from relations
        boolean firstField = true;
        for (Relation relation : queryRequestNew.getRelations()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append(String.join(", ", relation.getFields()));
            firstField = false;
        }

        sql.append(" FROM ").append(queryRequestNew.getRelations().get(0).getTable()); // Use the first relation's table as the main table

        // Handle joins based on the relations array
        for (int i = 1; i < queryRequestNew.getRelations().size(); i++) {
            Relation relation = queryRequestNew.getRelations().get(i);
            sql.append(" ").append(relation.getJoinType()).append(" JOIN ").append(relation.getTable())
                    .append(" ON ").append(queryRequestNew.getRelations().get(0).getTable()).append(".userId = ")
                    .append(relation.getTable()).append(".userId ");
        }

        // Append WHERE clause if filters exist
        if (queryRequestNew.getFilters() != null && !queryRequestNew.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", queryRequestNew.getFilters()));
        }

        // Append ORDER BY clause
        if (queryRequestNew.getOrderBy() != null && !queryRequestNew.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", queryRequestNew.getOrderBy()));
        }

        // Append GROUP BY clause if aggregateReport is TRUE
        if (queryRequestNew.getGroupBy() != null && !queryRequestNew.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", queryRequestNew.getGroupBy()));
        }

        return sql.toString(); // Return the constructed SQL query
    }

    // Format the SQL query for readability in logs
    private String formatSql(String sql) {
        return sql.replace(",", ",\n")
                .replace(" FROM", "\nFROM")
                .replace(" WHERE", "\nWHERE")
                .replace(" ORDER BY", "\nORDER BY")
                .replace(" GROUP BY", "\nGROUP BY")
                .replace(" JOIN", "\nJOIN")
                .replace(" ON", "\nON");
    }
}
