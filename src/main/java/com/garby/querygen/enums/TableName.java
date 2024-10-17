package com.garby.querygen.enums;

public enum TableName {
    CUSTOMER("customer"),
    TRANSACTION("transaction");

    private String value;

    TableName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
