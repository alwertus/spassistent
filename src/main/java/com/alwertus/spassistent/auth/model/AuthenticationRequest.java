package com.alwertus.spassistent.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
