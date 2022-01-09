package com.alwertus.spassistent.auth.model;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * get from application.properties all properties start with "prefix":
 * application.jwt.secretKey=...
 * application.jwt.tokenPrefix=...
 * application.jwt.tokenExpiration=...
 */

@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtProperties {
    @Getter @Setter
    private String secretKey;

    @Getter @Setter
    private String tokenPrefix;

    @Getter @Setter
    private Integer tokenExpiration;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey.getBytes());
    }

}