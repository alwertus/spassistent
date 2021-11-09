package com.alwertus.spassistent.user.controller;

import com.alwertus.spassistent.user.view.AuthenticationRequest;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.service.UserService;
import com.alwertus.spassistent.user.view.AuthenticationResponse;
import com.alwertus.spassistent.user.model.JwtToken;
import com.alwertus.spassistent.user.view.VerifyResponse;
import com.alwertus.spassistent.user.service.JwtService;
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
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest rq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(rq.getUsername(), rq.getPassword()));

            JwtToken token = jwtService.createNewToken(authentication);
            User user = userService.findUserByLogin(rq.getUsername());

            log.info("Token created for username = " + user.getLogin());
            return ResponseEntity.ok(new AuthenticationResponse(token, "2", user));
        } catch (BadCredentialsException e) {
            log.error("Error login. username=" + rq.getUsername() + ", password=" + rq.getPassword());
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?> verifyToken(@RequestBody AuthenticationRequest rq) {
        try {
            if (jwtService.authenticateWithToken(rq.getToken()))
                return ResponseEntity.ok(new VerifyResponse("Ok"));
            else {
                log.error("Wrong token");
                throw new BadCredentialsException("Wrong token");
            }
        } catch (BadCredentialsException e) {
            log.error("Error login. username=" + rq.getUsername() + ", password=" + rq.getPassword());
            throw new BadCredentialsException("Incorrect username or password");
        } catch (Exception e) {
            log.error("Unknown error while verify token. " + e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody AuthenticationRequest rq) {
        log.info("Refresh old token with " + rq);
        try {
            if (jwtService.authenticateWithToken(rq.getToken()))
                return ResponseEntity.ok(new VerifyResponse("Ok"));
            else {
                log.error("Wrong token");
                throw new BadCredentialsException("Wrong token");
            }
        } catch (BadCredentialsException e) {
            log.error("Error login. username=" + rq.getUsername() + ", password=" + rq.getPassword());
            throw new BadCredentialsException("Incorrect username or password");
        } catch (Exception e) {
            log.error("Unknown error while verify token. " + e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest rq) {
        String login = rq.getUsername();
        String password = rq.getPassword();

        userService.createUser(login, password);

        return createAuthenticateToken(rq);
    }
}
