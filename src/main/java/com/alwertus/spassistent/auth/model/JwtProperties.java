package com.alwertus.spassistent.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * get from application.properties all properties start with "prefix":
 * application.jwt.secretKey=...
 * application.jwt.tokenPrefix=...
 * application.jwt.tokenExpirationAfterDays=...
 */

@Component
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.jwt")
public class JwtProperties {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
}
