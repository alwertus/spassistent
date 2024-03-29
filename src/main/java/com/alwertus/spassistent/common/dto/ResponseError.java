package com.alwertus.spassistent.common.dto;

import lombok.Getter;

public class ResponseError extends Response {

    @Getter
    private final String error;

    public ResponseError(String error) {
        this.error = error;
    }

    @Override
    public String getResult() {
        return "Error";
    }
}
