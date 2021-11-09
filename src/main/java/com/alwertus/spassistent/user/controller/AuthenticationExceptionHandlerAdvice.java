package com.alwertus.spassistent.user.controller;

import com.alwertus.spassistent.user.view.AuthenticationResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Authentication error handler
 */
@ControllerAdvice
public class AuthenticationExceptionHandlerAdvice implements AuthenticationEntryPoint {

    // on error BadCredential
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleException(BadCredentialsException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new AuthenticationResponseError(e.getMessage()));
    }

    @Override
    public void commence(HttpServletRequest rq, HttpServletResponse rs, AuthenticationException e) throws IOException {
        rs.setStatus(rs.SC_FORBIDDEN);
        rs.getWriter().write("Error: Wrong token");
        rs.getWriter().flush();
    }
}
