package com.alwertus.spassistent.user.service;

import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) throws BadCredentialsException {
        log.info("Save user '{}' to DB", user.getLogin());
        /*log.info("Create new User: " + login + ":" + password);

        try {
            userRepo
                    .findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Login " + login + "not found"));
            throw new BadCredentialsException("User already exists");
        } catch (UsernameNotFoundException ignored) {}

        User user = new User();
        user.setLogin(login);*/
        if (user.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getNewPassword()));
            user.setNewPassword(null);
        }

        userRepo.save(user);
    }

    @Override
    public User getUser(String login) {
        log.debug("Fetching user '{}'", login);
        return userRepo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUser(authentication.getName());
    }
}
