package com.alwertus.spassistent.auth.jwt;


import com.alwertus.spassistent.auth.controller.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest rq,
                                    @NonNull HttpServletResponse rs,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = rq.getHeader(HttpHeaders.AUTHORIZATION);

        boolean authSuccess = false;
        try {
            authSuccess = jwtService.authenticateWithToken(authHeader);
        } catch (ExpiredJwtException e) {
            log.error("Token is expired");
        } catch (Exception e) {
            log.error("Token verifier error: " + e.getMessage() + "\nToken='" + authHeader + "'");
        }
        log.debug("Token verify result = " + (authSuccess ? "Success" : "Fail"));

        filterChain.doFilter(rq, rs);
    }
}
