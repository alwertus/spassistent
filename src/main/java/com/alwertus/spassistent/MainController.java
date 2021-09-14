package com.alwertus.spassistent;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    public MainController() {
        System.out.println("CONSTRUCTOR");
    }

//    @GetMapping("/hello")
    public String request() {
        System.out.println("ZOZO");
        return "helloMzfk";
    }
}
