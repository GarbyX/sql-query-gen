package com.garby.querygen.controllers;

    import com.garby.querygen.models.QueryRequest;
    import com.garby.querygen.models.Relation;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.media.Content;
    import io.swagger.v3.oas.annotations.media.Schema;
    import io.swagger.v3.oas.annotations.parameters.RequestBody;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    // import javax.management.relation.Relation;
    import java.util.HashMap;
    import java.util.Map;

@RestController
@RequestMapping("/api/v1/query")
public class QueryController {

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
        // SQL Query generation logic based on the request body
        String sqlQuery = generateSQLFromRequest(request);

        // Response map to hold the result
        Map<String, String> response = new HashMap<>();
        response.put("sql", sqlQuery);

        return ResponseEntity.ok(response);
    }

    private String generateSQLFromRequest(QueryRequest request) {
        // Base SQL Query
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ");

        // Handle SELECT fields
        request.getRelations().forEach(relation -> {
            relation.getFields().forEach(field -> queryBuilder.append(relation.getTable()).append(".").append(field).append(", "));
        });

        // Remove last comma and space
        queryBuilder.setLength(queryBuilder.length() - 2);

        // Handle FROM and JOIN clauses
        queryBuilder.append(" FROM ").append(request.getRelations().get(0).getTable()).append(" ");
        for (int i = 1; i < request.getRelations().size(); i++) {
            Relation relation = (Relation) request.getRelations().get(i);
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
}
