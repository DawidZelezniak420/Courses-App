package com.zelezniak.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping({"/login/page"})
    public String loginPage() {
        return "login";
    }

    @GetMapping({"/access-denied"})
    public String showAccessDenied() {
        return "access-denied";
    }
}