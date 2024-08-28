package com.sam.flightsearch.error;

public enum ErrorCode {
    INVALID_REQUEST("INVALID_REQUEST"),
    OTHER_ERROR("OTHER_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}