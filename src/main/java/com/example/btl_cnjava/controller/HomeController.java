package com.example.btl_cnjava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/privacy")
    public String privacyPolicy() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String termsPolicy() {
        return "terms";
    }
}
