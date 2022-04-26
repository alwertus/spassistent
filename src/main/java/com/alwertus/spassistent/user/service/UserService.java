package com.alwertus.spassistent.user.service;

import com.alwertus.spassistent.common.service.EmailSenderService;
import com.alwertus.spassistent.common.service.RandomStringGenerator;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator stringGenerator;
    private final EmailSenderService emailSenderService;

    @Value("${front.url}")
    private String frontUrl;

    public void createUser(String login, String password) {
        if (login == null || password == null)
            throw new RuntimeException("Login or Password cannot be empty");
        
        User user = new User();
        try {
            getUser(login);
            throw new RuntimeException(String.format("User with login '%s' already exists", login));
        } catch (UsernameNotFoundException ignored) {

        }

        String confirmString = stringGenerator.generateString(50);
        emailSenderService.sendSimpleMessage(login, "Confirmation of creating a new account on SinglePlace Assistent. " + new Date(),
                String.format("Click here: '%s/emailConfirm/%s' to confirm registration with user: %s. Or ignore this email if you don't want",
                        frontUrl, confirmString, login)
                );

        user.setLogin(login);
        user.setNewPassword(password);
        user.setEmailConfirmString(confirmString);
        user.setCreated(new Date());

        try {
            saveUser(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Error while save user. " + ex.getMessage());
            throw new RuntimeException("Login is busy");
        }
    }

    public void saveUser(User user) throws BadCredentialsException {
        log.info("Save user '{}' to DB", user.getLogin());
        if (user.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getNewPassword()));
            user.setNewPassword(null);
        }

        userRepo.save(user);
    }

    public User getUser(String login) {
        User user =  userRepo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found. Login='" + login + "'"));

        if (user.getEmailConfirmString() != null)
            throw new RuntimeException("User email not verified");

        return user;
    }

    public User getUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found. Id='" + id + "'"));

        if (user.getEmailConfirmString() != null)
            throw new RuntimeException("User email not verified");

        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUser(authentication.getName());
    }

    public void confirmEmail(String secret) {
        User user = userRepo.findByEmailConfirmString(secret)
                .orElseThrow(() -> new RuntimeException("Confirmation string is wrong"));
        user.setEmailConfirmString(null);
        userRepo.save(user);
        log.info("Email success confirmed for user id=" + user.getId() + ", login=" + user.getLogin());
    }
}
