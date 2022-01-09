package com.alwertus.spassistent.auth.controller;


import com.alwertus.spassistent.auth.service.TokenService;
import com.alwertus.spassistent.auth.view.AuthenticationResponseOk;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

@Log4j2
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/refresh")
    public AuthenticationResponseOk refreshToken(HttpServletRequest rq, HttpServletResponse rs) {
        User user = userService.getCurrentUser();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new AuthenticationResponseOk(
                tokenService.generateToken(user.getLogin(), rq.getRequestURL().toString(), authorities, false),
                tokenService.generateToken(user.getLogin(), rq.getRequestURL().toString(), authorities, true));
    }
}
