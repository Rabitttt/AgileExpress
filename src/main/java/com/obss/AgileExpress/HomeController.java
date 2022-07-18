package com.obss.AgileExpress;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page!";
    }
    @GetMapping("/restricted")
    public String restricted() {
        return "If you see that you are authenticated";
    }

}

