package com.alwertus.spassistent.user.service;

import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.model.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String login, String password) throws BadCredentialsException {
        log.info("Create new User: " + login + ":" + password);

        try {
            userRepository
                    .findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Login " + login + "not found"));
            throw new BadCredentialsException("User already exists");
        } catch (UsernameNotFoundException ignored) {}

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findUserByLogin(authentication.getName());
    }
}
