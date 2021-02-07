package com.tencorners.springbootthymleaftomcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login() {
        return "authentication/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/index";
    }

    @GetMapping("/authentication/user")
    public String userStatusCheck() {
        return "authentication/user";
    }

    @GetMapping("/authentication/admin")
    public String adminStatusCheck() {
        return "authentication/admin";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "authentication/access-denied";
    }

}
