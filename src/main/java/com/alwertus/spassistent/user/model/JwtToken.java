package com.alwertus.spassistent.user.model;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

@Log4j2
public class JwtToken {

    @Getter @Setter
    private String userName;

    @Getter @Setter
    private Date expirationDate;

    @Getter @Setter
    private Collection<? extends GrantedAuthority> authorities;

    @Getter
    private Date created;

    private final SecretKey secretKey;

    private final JwtProperties jwtProperties;

    public JwtToken(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String getTokenString() {
        created = new Date();
        expirationDate = new Date(created.getTime() + (jwtProperties.getTokenExpirationAfterDays() * 60 * 1000));

        return jwtProperties.getTokenPrefix() + Jwts.builder()
                .setSubject(userName)
                .claim("authorities", authorities)
                .setIssuedAt(created)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public void parseFromString(String tokenStringRaw) throws Exception {

        if (Strings.isNullOrEmpty(tokenStringRaw) || !tokenStringRaw.startsWith(jwtProperties.getTokenPrefix()))
            throw new Exception("The token is expected to start with: " + jwtProperties.getTokenPrefix());

        String tokenString = tokenStringRaw.replace(jwtProperties.getTokenPrefix(), "");

        Claims body = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(tokenString)
                .getBody();

        setUserName(body.getSubject());
        setExpirationDate(body.getExpiration());
        created = body.getIssuedAt();
        setAuthorities((Collection<? extends GrantedAuthority>) body.get("authorities"));
    }

    @Override
    public String toString() {
        return "JwtToken{" +
                "userName='" + userName + '\'' +
                ", expirationDate=" + expirationDate +
                ", authorities=" + authorities +
                ", created=" + created +
                '}';
    }
}
