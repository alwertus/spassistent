package com.alwertus.spassistent.user.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
    private String token;
    private String refreshToken;
}