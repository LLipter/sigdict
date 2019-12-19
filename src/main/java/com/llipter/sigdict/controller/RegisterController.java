package com.llipter.sigdict.controller;

import com.llipter.sigdict.Utility;
import com.llipter.sigdict.entity.Session;
import com.llipter.sigdict.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController extends SessionController {

    @GetMapping(path = "/register.html")
    public String getRegisterPage() {
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

        if (validateSession(request)) {
            Utility.addErrorMessage(model, "PLEASE LOG OUT FIRST");
            return "register";
        }

        if (!Utility.isValidUsername(username)) {
            Utility.addErrorMessage(model, "INVALID USERNAME");
            return "register";
        }

        if (!Utility.isValidPassword(password)) {
            Utility.addErrorMessage(model, "INVALID PASSWORD");
            return "register";
        }

        if (!password.equals(passwordConfirmed)) {
            Utility.addErrorMessage(model, "PLEASE CONFIRM YOUR PASSWORD");
            return "register";
        }

        if (!Utility.isValidEmail(email)) {
            Utility.addErrorMessage(model, "INVALID EMAIL");
            return "register";
        }

        if (userRepository.findByUsername(username) != null) {
            Utility.addErrorMessage(model, "DUPLICATE USERNAME");
            return "register";
        }

        // register successfully
        // set new session
        User user = new User(username, password, email);
        user.setSession(new Session(user));
        // set cookie
        Session.setSessionCookie(response, user.getSession().getSessionId());
        userRepository.save(user);

        return "main";
    }
}
