package com.llipter.sigdict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @GetMapping(path="/register.html")
    public String getRegisterPage(){
        return "register";
    }

    @PostMapping(path = "/register")
    public String register(@RequestParam(name = "username", required = true) String username,
                           @RequestParam(name = "password", required = true) String password,
                           @RequestParam(name = "password-confirm", required = true) String passwordConfirmed,
                           @RequestParam(name = "email", required = true) String email,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        return "main";
    }
}
