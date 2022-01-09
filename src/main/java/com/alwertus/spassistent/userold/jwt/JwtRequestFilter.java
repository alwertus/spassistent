package com.alwertus.spassistent.userold.jwt;


import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class JwtRequestFilter {
   /* private final JwtService jwtService;

    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    protected void doFilterInternal(HttpServletRequest rq,
                                    @NonNull HttpServletResponse rs,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = rq.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            jwtService.authenticateWithToken(authHeader);
            filterChain.doFilter(rq, rs);
        *//*} catch (ExpiredJwtException e) {
            rs.setStatus(HttpServletResponse.SC_GONE);
            writeError(rs, "Token is expired");*//*
        } catch (Exception e) {
            rs.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writeError(rs, "Token verifier error: " + e.getMessage() + "\nToken='" + authHeader + "'");
        }
    }

    private void writeError(@NonNull HttpServletResponse rs, String errorText) throws IOException {
        log.error(errorText);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("error", errorText);
        PrintWriter out = rs.getWriter();
        rs.setCharacterEncoding("UTF-8");
        rs.setContentType(MediaType.APPLICATION_JSON.getType());
        out.print(jsonResponse);
        out.flush();
    }*/
}
