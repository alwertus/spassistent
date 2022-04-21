package com.alwertus.spassistent.auth.controller;

import com.alwertus.spassistent.auth.model.JwtProperties;
import com.alwertus.spassistent.common.view.ResponseError;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProperties jwtProperties;
    private final List<String> noAuthList;

    @Override
    protected void doFilterInternal(HttpServletRequest rq, @NonNull HttpServletResponse rs, @NonNull FilterChain filterChain) throws ServletException, IOException {
        StringBuilder sLog = new StringBuilder();
        if (noAuthList.contains(rq.getServletPath())) {
            log.info("Skip authorization with token");
            filterChain.doFilter(rq, rs);
        } else {
            sLog.append("Try to authorization with token => ");
            String authorizationHeader = rq.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(jwtProperties.getTokenPrefix())) {
                try {
                    sLog.append("token => ");
                    String token = authorizationHeader.substring(jwtProperties.getTokenPrefix().length());
                    sLog.append("verifier => ");
                    JWTVerifier verifier = JWT.require(jwtProperties.getAlgorithm()).build();
                    sLog.append("decode => ");
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    sLog.append("foreach => ");
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    sLog.append("newToken => ");
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    sLog.append("set Auth => ");
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    sLog.append("END >> Next Filter.");
                    log.trace(sLog);
                    filterChain.doFilter(rq, rs);
                } catch (Exception e) {
                    sLog.append("Error login in: ").append(e.getMessage());
                    log.trace("Error: " + sLog);

                    rs.setStatus(UNAUTHORIZED.value());
                    rs.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(rs.getOutputStream(), new ResponseError(e.getMessage()));
                }

            } else {
                sLog.append(authorizationHeader == null
                        ? "Empty Authorization header"
                        : "Authorization header not start with '" + jwtProperties.getTokenPrefix() + "'");
                log.error(sLog);

                filterChain.doFilter(rq, rs);
            }
        }
    }

}
