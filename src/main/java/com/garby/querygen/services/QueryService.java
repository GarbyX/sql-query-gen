package com.garby.querygen.services;

import com.garby.querygen.models.QueryRequest;
import com.garby.querygen.models.Relation;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    public String buildSqlQuery(QueryRequest queryRequest) {
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


