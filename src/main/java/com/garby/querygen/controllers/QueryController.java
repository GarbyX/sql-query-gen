package com.garby.querygen.controllers;

    import com.garby.querygen.models.QueryRequest;
    import com.garby.querygen.services.SqlQueryService;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.media.Content;
    import io.swagger.v3.oas.annotations.media.Schema;
    import io.swagger.v3.oas.annotations.parameters.RequestBody;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.Map;

@RestController
@RequestMapping("/api/v1/query")
public class QueryController {

    private final SqlQueryService sqlqueryService;

    public QueryController(SqlQueryService sqlqueryService) {
        this.sqlqueryService = sqlqueryService;
    }

    @Operation(
            summary = "Generate SQL query based on request body",
            description = "Returns SQL query for customer and transaction tables based on input.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = QueryRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully generated SQL query"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateSQLQuery(@RequestBody QueryRequest request) {
        // Log the request object
        System.out.println("Received request: " + request);
        // SQL Query generation logic based on the request body
        if (request.getRelations() == null || request.getRelations().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Relations cannot be null or empty"));
        }
        String sqlQuery =  sqlqueryService.generateSQLFromRequest(request);
        // Response map to hold the result
        Map<String, String> response = new HashMap<>();
        response.put("sql", sqlQuery);
        return ResponseEntity.ok(response);
    }

    //    @Operation(summary = "Process an SQL query request & returns the query. (query object logged)",
//            description = "Processes a SQL query request with options for table names and fields. Received request is logged as an object in the terminal/logs.",
//            responses = {@ApiResponse(responseCode = "200", description = "Query processed successfully")})
//    @PostMapping("/api/v1/process")
//    public ResponseEntity<String> processQuery(@RequestBody QueryRequest queryRequest) {
//        String sqlQuery = queryService.buildSqlQuery(queryRequest);  // Custom method to build SQL query
//        return ResponseEntity.ok(sqlQuery);  // Return the actual query instead of a success message
//    }


}
