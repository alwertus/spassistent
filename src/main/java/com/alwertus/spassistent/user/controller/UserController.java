package com.alwertus.spassistent.user.controller;

import com.alwertus.spassistent.common.dto.Response;
import com.alwertus.spassistent.common.dto.ResponseError;
import com.alwertus.spassistent.common.dto.ResponseOk;
import com.alwertus.spassistent.user.model.User;
import com.alwertus.spassistent.user.service.UserService;
import com.alwertus.spassistent.user.view.EmailConfirmRequest;
import com.alwertus.spassistent.user.view.MyInfoResponse;
import com.alwertus.spassistent.user.view.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

        try {
            userService.createUser(rq.getLogin(), rq.getPassword());
        } catch (Exception e) {
            return new ResponseError(e.getMessage());
        }

        return new ResponseOk();
    }

    @PostMapping(value = "/myInfo")
    public MyInfoResponse getMyInfo() {
        return new MyInfoResponse(userService.getCurrentUser());
    }

    @PostMapping(value = "/emailConfirm")
    public Response emailConfirm(@RequestBody EmailConfirmRequest rq) {
        log.info("Try to confirm email with string: " + rq.getConfirmString());
        try {
            userService.confirmEmail(rq.getConfirmString());
        } catch (Exception e) {
            return new ResponseError(e.getMessage());
        }
        return new ResponseOk();
    }

}
