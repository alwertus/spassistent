package com.alwertus.spassistent.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Log4j2
public class CorsConfig extends UrlBasedCorsConfigurationSource {

    public CorsConfig(CorsProperties corsConfig) {
        if (corsConfig.getOrigins().isEmpty())
            log.error("Parameter 'application.allowed.origins' is EMPTY");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList(corsConfig.getOrigins().split(",")));
        configuration.setAllowedMethods(Arrays.asList(corsConfig.getMethods().split(",")));
        configuration.setAllowedHeaders(Arrays.asList(corsConfig.getHeaders().split(",")));

        this.registerCorsConfiguration("/**", configuration);
    }
}
