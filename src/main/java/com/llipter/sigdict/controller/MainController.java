package com.llipter.sigdict.controller;

import com.llipter.sigdict.Utility;
import com.llipter.sigdict.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController extends SessionController {
    @RequestMapping(value = "/main.html", method = RequestMethod.GET)
    public String loginPage(Model model, HttpServletRequest request) {
        if (!validateSession(request)) {
            // haven't signed in
            Utility.addErrorMessage(model, "PLEASE SIGN IN FIRST");
            return "login";
        }

        User user = getUserFromSession(request);
        Utility.addUserMessage(model, user);
        return "main";
    }
}
