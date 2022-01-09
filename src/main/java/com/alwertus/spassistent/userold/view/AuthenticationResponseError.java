package com.alwertus.spassistent.userold.view;

public record AuthenticationResponseError(String error) {


    @SuppressWarnings("unused")
    public String getError() {
        return error;
    }

    @SuppressWarnings("unused")
    public String getResult() { return "Error"; }
}
