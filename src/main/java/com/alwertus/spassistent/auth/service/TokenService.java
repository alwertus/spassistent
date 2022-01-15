package com.alwertus.spassistent.auth.service;

import com.alwertus.spassistent.auth.model.JwtProperties;
import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProperties jwtProperties;

    public String generateToken(String userName, String issuer, Collection<GrantedAuthority> userAuthorities, boolean isRefreshToken) {

        StringBuilder sb = new StringBuilder(String.format("Generate token with parameters: userName='%s', issuer='%s', isRefreshToken='%s' =>", userName, issuer, isRefreshToken));
        try {
            Date expireDate = new Date(System.currentTimeMillis() + (long) (isRefreshToken ? 100 : 1) * jwtProperties.getTokenExpiration() * 60 * 1000);
            log.debug("-2");
            List<String> claim = userAuthorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            log.debug("-3");
            String token = jwtProperties.getTokenPrefix() +
                    JWT.create()
                            .withSubject(userName)
                            .withExpiresAt(expireDate)
                            .withIssuer(issuer)
                            .withClaim("roles", claim)
                            .sign(jwtProperties.getAlgorithm());
            sb.append("Success: ").append(token);
            return token;
        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            log.trace(sb);
        }

    }
}
