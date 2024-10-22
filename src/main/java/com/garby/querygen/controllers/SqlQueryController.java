package com.garby.querygen.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garby.querygen.models.QueryRequest;
import com.garby.querygen.models.QueryRequestNew;
import com.garby.querygen.services.QueryService;
import com.garby.querygen.services.SqlQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class SqlQueryController {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryController.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper instance

    private final QueryService queryService;
    private final SqlQueryService sqlQueryService;

    @Autowired       // used as there is more than one constructor
    public SqlQueryController(QueryService queryService, SqlQueryService sqlQueryService) {
        this.queryService = queryService;
        this.sqlQueryService = sqlQueryService;
    }

    @Operation(summary = "Process an SQL query request & returns the query. (query object logged)",
            description = "Processes a SQL query request with options for table names and fields. Received request is logged as an object in the terminal/logs.",
            responses = {@ApiResponse(responseCode = "200", description = "Query processed successfully")})
    @PostMapping("/api/v1/process")
    public ResponseEntity<String> processQuery(@RequestBody QueryRequest queryRequest) {
        String sqlQuery = queryService.buildSqlQuery(queryRequest);  // Custom method to build SQL query
        return ResponseEntity.ok(sqlQuery);  // Return the actual query instead of a success message
    }

    @Operation(summary = "Process an SQL query request & return the query as output. (formatted query logged)",
            description = "Processes a SQL query request with options for table names and fields. Received request is formatted then logged for better readability in the terminal/logs.",
            responses = {@ApiResponse(responseCode = "200", description = "Query processed successfully")})
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

    @Operation(summary = "Process an SQL query request, format logging & return the query as output",
            description = "Processes a SQL query request with options for table names and fields. Formatted logging for both input and output.",
            responses = {@ApiResponse(responseCode = "200", description = "Query processed successfully")})
            // SELECT string FROM customer WHERE string ORDER BY string GROUP BY string
    @PostMapping("/api/v1/sqlquery2")
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
            String sqlQuery = queryService.buildSqlQuery(queryRequest);
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


    @Operation(summary = "Process an SQL query request (table names, fields & aggregation function)",
            description = "Processes an SQL query request with options for table names, fields, and aggregation functions. Formatted logging for both input and output.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Query processed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request format or validation error")
            }
            // SELECT string FROM string WHERE string ORDER BY transaction.transDate GROUP BY string LEFT JOIN string ON string.string = string.string
    )
    @PostMapping("/api/v1/sqlquery3")
    public ResponseEntity<?> handleQuery3(@RequestBody QueryRequestNew queryRequest) {
        try {
            String sqlQuery = sqlQueryService.processQuery(queryRequest);
            return ResponseEntity.ok(sqlQuery);      // Return the constructed SQL query
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request format");
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}


