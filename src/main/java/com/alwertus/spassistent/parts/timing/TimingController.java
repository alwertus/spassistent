package com.alwertus.spassistent.parts.timing;

import com.alwertus.spassistent.common.dto.Response;
import com.alwertus.spassistent.common.dto.ResponseOk;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timing")
public class TimingController {

    @PostMapping("/get")
    public Response get() {

        return new ResponseOk();
    }
}
