package com.garby.querygen.enums;

public enum TransactionField {
    TRANSDATE("transDate"),
    AMOUNT("amount"),
    REFERENCE("reference"),
    STATUS("status"),
    TRANSACTIONUSER("transactionUser");

    private String value;

    TransactionField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}



