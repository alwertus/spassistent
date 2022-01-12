package com.alwertus.spassistent.config;

import com.alwertus.spassistent.auth.controller.CustomAuthenticationFilter;
import com.alwertus.spassistent.auth.controller.CustomAuthorizationFilter;
import com.alwertus.spassistent.auth.model.JwtProperties;
import com.alwertus.spassistent.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProperties jwtProperties;
    private final CorsProperties corsConfig;
    private final TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] permitUrls = new String[] {
                "/api/auth/login",
                "/api/user/register"};

        http
                .cors()
                .and().csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(new CustomAuthenticationFilter("/api/auth/login", authenticationManager(), tokenService))
                    .addFilterBefore(new CustomAuthorizationFilter(jwtProperties, Arrays.stream(permitUrls).toList()), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests()
                .antMatchers( permitUrls).permitAll()
                .antMatchers(HttpMethod.POST,
                        "/api/user/myInfo")
                .authenticated()
                .anyRequest().authenticated();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfig(corsConfig);
    }


    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }*/
/*
    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userSecurityService);
        return daoAuthenticationProvider;
    }*/
}
