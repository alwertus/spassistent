package com.alwertus.spassistent.auth.controller;

import com.alwertus.spassistent.auth.service.TokenService;
import com.alwertus.spassistent.auth.view.AuthenticationRequest;
import com.alwertus.spassistent.auth.view.AuthenticationResponseOk;
import com.alwertus.spassistent.common.view.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * authentication and generate token handler
 */

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public CustomAuthenticationFilter(String url, AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
        this.setFilterProcessesUrl(url);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        StringBuilder sLog = new StringBuilder();
        response.setContentType(APPLICATION_JSON_VALUE);

        try {
            AuthenticationRequest rq = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationRequest.class);

            String login = rq.getUsername();
            String password = rq.getPassword();
            sLog.append(String.format("Attempt authentication with login='%s' password='%s' >> ", login, password));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
            Authentication authResult = authenticationManager.authenticate(token);

            sLog.append(authResult.isAuthenticated() ? "Success" : "Fail");

            return authResult;
        } catch (AuthenticationException | IOException e) {
            sLog.append(String.format("Fail (%s)", e.getMessage()));
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), new ResponseError(e.getMessage()));
            } catch (IOException ioException) {
                log.error("Cannot write response: " + ioException.getMessage());
            }
            throw new AuthenticationServiceException(e.getMessage(), e);
        } finally {
            log.trace(sLog.toString());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();

        String accessToken = tokenService.generateToken(user.getUsername(), request.getRequestURL().toString(), user.getAuthorities(), false);
        String refreshToken = tokenService.generateToken(user.getUsername(), request.getRequestURL().toString(), user.getAuthorities(), true);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new AuthenticationResponseOk(accessToken, refreshToken));
    }



}
