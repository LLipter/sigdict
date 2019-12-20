package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.Utility;
import com.llipter.sigdict.entity.Session;
import com.llipter.sigdict.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                           HttpServletRequest request,
                           HttpServletResponse response,
                           RedirectAttributes redirectAttributes) {

        if (validateSession(request)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.LOGOUT_FIRST);
            return "redirect:/register.html";
        }

        if (!Utility.isValidUsername(username)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.USERNAME_INVALID);
            return "redirect:/register.html";
        }

        if (!Utility.isValidPassword(password)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INVALID);
            return "redirect:/register.html";
        }

        if (!password.equals(passwordConfirmed)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_MISMATCHED);
            return "redirect:/register.html";
        }

        if (!Utility.isValidEmail(email)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_INVALID);
            return "redirect:/register.html";
        }

        if (userRepository.findByUsername(username) != null) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.USERNAME_DUPLICATE);
            return "redirect:/register.html";
        }

        // register successfully
        // set new session
        User user = new User(username, password, email);
        user.setSession(new Session(user));
        // set cookie
        Session.setSessionCookie(response, user.getSession().getSessionId());
        userRepository.save(user);

        return "redirect:/main.html";
    }
}
