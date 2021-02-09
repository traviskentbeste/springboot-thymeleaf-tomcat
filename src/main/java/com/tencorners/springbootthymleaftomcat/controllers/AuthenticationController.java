package com.tencorners.springbootthymleaftomcat.controllers;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        System.out.printf("session : " + session);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            System.out.printf("ex : " + ex + "\n");
            System.out.printf("ex : " + ex.getMessage() + "\n");

            model.addAttribute("hasErrors", 1);
            model.addAttribute("errorMessage", ex.getMessage());
        } else {
            model.addAttribute("hasErrors", 0);
        }

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

    @GetMapping("/authentication/access-denied")
    public String accessDenied() {
        return "authentication/access-denied";
    }

}
