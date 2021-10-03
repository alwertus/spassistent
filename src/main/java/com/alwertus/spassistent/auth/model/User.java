package com.alwertus.spassistent.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class User {

    // return User. Adopt for Spring Security format
    public UserDetails getSecurityUser() {
        return new UserDetails() {
            @Override public Collection<? extends GrantedAuthority> getAuthorities() { return new ArrayList<>(); }
            @Override public String getUsername() { return "user"; }
            @Override public String getPassword() { return "$2a$10$80LxVi4Xw2I7BYvaZpNK5Oa9txNKkxNY6lmP4p269bF441Rscep3a"; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isEnabled() { return true; }
        };
    }
}
