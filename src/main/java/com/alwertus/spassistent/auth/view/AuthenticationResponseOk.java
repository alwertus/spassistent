package com.alwertus.spassistent.auth.view;

import com.alwertus.spassistent.common.dto.ResponseOk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationResponseOk extends ResponseOk {
    private final String accessToken;
    private final String refreshToken;
}
