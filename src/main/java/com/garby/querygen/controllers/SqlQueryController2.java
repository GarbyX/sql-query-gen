package com.garby.querygen.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.garby.querygen.models.DefaultValueMappingEntity;
import com.garby.querygen.models.QueryRequestNew;
import com.garby.querygen.services.SqlQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SqlQueryController2 {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryController2.class);
    private final SqlQueryService sqlQueryService;
    // private final ObjectMapper objectMapper;

    @Autowired
    public SqlQueryController2(SqlQueryService sqlQueryService) {
        this.sqlQueryService = sqlQueryService;
    }

    @PostMapping("/api/v1/sqlquery2")
    public ResponseEntity<?> handleQuery(@RequestBody QueryRequestNew queryRequest) {
        try {
            // Process the query through the service
            String sqlQuery = sqlQueryService.processQuery(queryRequest);
            return ResponseEntity.ok(sqlQuery);         // Return the constructed SQL query
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid request format");
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
