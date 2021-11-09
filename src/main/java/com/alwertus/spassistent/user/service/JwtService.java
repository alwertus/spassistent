package com.alwertus.spassistent.user.service;

import com.alwertus.spassistent.user.model.JwtProperties;
import com.alwertus.spassistent.user.model.JwtToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Get Token object from input string request header
     */
    public boolean authenticateWithToken(String sToken) throws Exception {

        JwtToken token = new JwtToken(jwtProperties);
        token.parseFromString(sToken);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                token.getUserName(),
                null,
                token.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }

    public JwtToken createNewToken(Authentication authResult) {
        JwtToken token = new JwtToken(jwtProperties);
        token.setUserName(authResult.getName());
        token.setAuthorities(authResult.getAuthorities());

        return token;
    }

}
