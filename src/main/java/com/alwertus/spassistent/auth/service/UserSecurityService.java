package com.alwertus.spassistent.auth.service;

import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> oUser =  userRepo
                .findByLogin(login);
        oUser.ifPresentOrElse(
                (user) -> log.trace("Found user '{}'", login),
                () -> log.trace("User {} not found", login));

        User user = oUser.orElseThrow(() -> new UsernameNotFoundException("User '" + login + "' not found"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                authorities);
    }
}
