package com.garby.querygen.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garby.querygen.models.QueryRequest;
import com.garby.querygen.models.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class SqlQueryController {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryController.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper instance

    @PostMapping("/api/v1/sqlquery")
    public ResponseEntity<?> handleQuery(@RequestBody QueryRequest queryRequest) {
        try {
            // Convert the request object to JSON string for logging
            String jsonPayload = objectMapper.writeValueAsString(queryRequest);
            logger.info("Received request: {}", jsonPayload); // Log the JSON payload

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print
                    String formattedJson = objectMapper.writeValueAsString(queryRequest);
                    logger.info("Received request:\n{}", formattedJson);
                } catch (Exception e) {
                    logger.error("Error formatting request: {}", e.getMessage());
                }

            // Validate relations
            if (queryRequest.getRelations() == null ||
                    queryRequest.getRelations().isEmpty()) {
                throw new IllegalArgumentException("Relations cannot be null or empty");
            }
            // Process the query...
            return ResponseEntity.ok("Query processed successfully");
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request format");
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------------

    @PostMapping("/api/v1/query")
    public ResponseEntity<?> handleQuery2(@RequestBody QueryRequest queryRequest) {
        try {
            // Convert the request object to JSON string for logging
            String jsonPayload = objectMapper.writeValueAsString(queryRequest);
            logger.info("Received request: {}", jsonPayload); // Log the JSON payload

            try {
                // ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print
                String formattedJson = objectMapper.writeValueAsString(queryRequest);
                logger.info("Received request:\n{}", formattedJson);
            } catch (Exception e) {
                logger.error("Error formatting request: {}", e.getMessage());
            }

            // Validate relations
            if (queryRequest.getRelations().isEmpty()) {
                throw new IllegalArgumentException("Relations cannot be null or empty");
            }

            // Build SQL query
            String sqlQuery = buildSqlQuery(queryRequest);
            // logger.info("Generated SQL Query: {}", sqlQuery); // Log the generated SQL query
            logger.info("Generated SQL Query:\n{}", sqlQuery.replace(", ", ",\n  ")); // Log the generated SQL query with formatting

            return ResponseEntity.ok(sqlQuery); // Return the constructed SQL query
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request format");
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String buildSqlQuery(QueryRequest queryRequest) {
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

        sql.append(" FROM customer ");

        // Handle joins
        for (Relation relation : queryRequest.getRelations()) {
            if ("left join".equalsIgnoreCase(relation.getJoinType())) {
                sql.append("LEFT JOIN ").append(relation.getTable()).append(" ON customer.userId = ").append(relation.getTable()).append(".userId ");
            } else if ("inner join".equalsIgnoreCase(relation.getJoinType())) {
                sql.append("INNER JOIN ").append(relation.getTable()).append(" ON customer.userId = ").append(relation.getTable()).append(".userId ");
            }
        }

        // Append WHERE clause if filters exist
        if (queryRequest.getFilters() != null && !queryRequest.getFilters().isEmpty()) {
            sql.append("WHERE ");
            sql.append(String.join(" AND ", queryRequest.getFilters()));
        }

        // Append ORDER BY clause
        if (queryRequest.getOrderBy() != null && !queryRequest.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", queryRequest.getOrderBy()));
        }

        // Append GROUP BY clause if needed
        if (queryRequest.getGroupBy() != null && !queryRequest.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", queryRequest.getGroupBy()));
        }

        return sql.toString();
    }






}


