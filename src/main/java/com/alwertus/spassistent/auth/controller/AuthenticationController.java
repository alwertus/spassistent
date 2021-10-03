package com.alwertus.spassistent.auth.controller;

import com.alwertus.spassistent.auth.model.AuthenticationRequest;
import com.alwertus.spassistent.auth.model.AuthenticationResponse;
import com.alwertus.spassistent.auth.model.JwtToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest rq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(rq.getUsername(), rq.getPassword()));

            JwtToken token = jwtService.createNewToken(authentication);

            log.info("Token created for username = " + rq.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (BadCredentialsException e) {
            log.error("Error login. username=" + rq.getUsername() + ", password=" + rq.getPassword());
            throw new BadCredentialsException("Incorrect username or password");
        }

    }
}
