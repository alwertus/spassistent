package com.alwertus.spassistent.tmp;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class TempController {

    @RequestMapping("/bye")
    @ResponseBody
    public List<String> tempRequest() {
        return Arrays.asList("S1", "S2");
//        return "ZAZA";
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String welcome() {
        return "Welcome to RestTemplate Example.";
    }
}
