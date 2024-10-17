package com.garby.querygen.enums;

public enum CustomerField {
    USER_ID("userId"),
    USERNAME("username"),
    DOB("DOB");

    private String value;

    CustomerField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
