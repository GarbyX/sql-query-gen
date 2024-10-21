package com.garby.querygen.models;

public class DefaultValueMappingEntity {

    private String sourceTable;
    private String sourceField;
    private String targetTable;
    private String targetField;

    public DefaultValueMappingEntity() {
    }

    public DefaultValueMappingEntity(String sourceTable, String sourceField, String targetTable, String targetField) {
        this.sourceTable = sourceTable;
        this.sourceField = sourceField;
        this.targetTable = targetTable;
        this.targetField = targetField;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    // toString method for logging/debugging purposes
    @Override
    public String toString() {
        return "DefaultValueMappingEntity{" +
                "sourceTable='" + sourceTable + '\'' +
                ", sourceField='" + sourceField + '\'' +
                ", targetTable='" + targetTable + '\'' +
                ", targetField='" + targetField + '\'' +
                '}';
    }
}

