package com.alwertus.spassistent.user.controller;

import com.alwertus.spassistent.user.view.UserInfoRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class UserOperationsController {

    @PostMapping(value = "/update-user")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody UserInfoRequest rq) {
        log.info("Update user info " + rq.getFirstName() + ", " + rq.getLastName());

        return ResponseEntity.ok("bzbzbz");
    }

}
