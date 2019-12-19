package com.llipter.sigdict.controller;

import com.llipter.sigdict.Utility;
import com.llipter.sigdict.entity.Session;
import com.llipter.sigdict.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController extends SessionController {


    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(name = "username", required = true) String username,
                        @RequestParam(name = "password", required = true) String password,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        if (validateSession(request)) {
            // user already signed in
            Utility.addErrorMessage(model, "YOU HAVE ALREADY SIGNED IN");
            return "login";
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            Utility.addErrorMessage(model, "USER NOT EXISTED");
            return "login";
        } else if (!user.validatePassword(password)) {
            Utility.addErrorMessage(model, "PASSWORD INCORRECT");
            return "login";
        }

        // login successfully
        // set new session
        user.setSession(new Session(user));
        // set cookie
        Session.setSessionCookie(response, user.getSession().getSessionId());
        userRepository.save(user);

        return "redirect:/main.html";
    }
}