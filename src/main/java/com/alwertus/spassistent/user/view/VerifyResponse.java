package com.alwertus.spassistent.user.view;

public record VerifyResponse(String result) {
    private String getResult() {
        return result;
    }
}
