package com.alwertus.spassistent.auth.model;

public record AuthenticationResponse(JwtToken token) {

    @SuppressWarnings("unused")
    public String getToken() {
        return token.getTokenString();
    }
}
