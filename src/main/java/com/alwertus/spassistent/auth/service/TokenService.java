package com.alwertus.spassistent.auth.service;

import com.alwertus.spassistent.auth.model.JwtProperties;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProperties jwtProperties;

    public String generateToken(String userName, String issuer, Collection<GrantedAuthority> userAuthorities, boolean isRefreshToken) {
        log.trace(String.format("Generate token with parameters: userName='%s', issuer='%s', isRefreshToken='%s'", userName, issuer, isRefreshToken));
        return jwtProperties.getTokenPrefix() +
                JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + (long) (isRefreshToken ? 100 : 1) * jwtProperties.getTokenExpiration() * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles",
                        userAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .sign(jwtProperties.getAlgorithm());
    }
}
