package com.alwertus.spassistent.userold.view;

public record VerifyResponse(String result) {
    private String getResult() {
        return result;
    }
}
