package com.alwertus.spassistent.user.controller;

import com.alwertus.spassistent.common.view.Response;
import com.alwertus.spassistent.common.view.ResponseError;
import com.alwertus.spassistent.common.view.ResponseOk;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.service.UserService;
import com.alwertus.spassistent.user.view.MyInfoResponse;
import com.alwertus.spassistent.user.view.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/update")
    public ResponseOk updateUser(@RequestBody UserInfoRequest rq) {
        log.info("Update user info: " + rq);
        User user = userService.getCurrentUser();
        if (rq.getFirstName() != null)
            user.setFirstName(rq.getFirstName());
        if (rq.getLastName() != null)
            user.setLastName(rq.getLastName());

        userService.saveUser(user);

        return new ResponseOk();
    }

    @PostMapping(value = "/register")
    public Response createUser(@RequestBody UserInfoRequest rq) {
        log.info("Create new user: " + rq);

        if (rq.getLogin() == null || rq.getPassword() == null)
            return new ResponseError("Login or Password cannot be empty");

        User user = new User();
        try {
            userService.getUser(rq.getLogin());
            return new ResponseError(String.format("User with login '%s' already exists", rq.getLogin()));
        } catch (UsernameNotFoundException ignored) {

        }

        user.setLogin(rq.getLogin());
        user.setNewPassword(rq.getPassword());
        userService.saveUser(user);

        return new ResponseOk();
    }

    @PostMapping(value = "myInfo")
    public MyInfoResponse getMyInfo() {
        return new MyInfoResponse(userService.getCurrentUser());
    }

}
