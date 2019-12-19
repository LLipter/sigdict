package com.llipter.sigdict.controller;

import com.llipter.sigdict.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController extends SessionController {

    @PostMapping(path = "/logout")
    public String logout(
            Model model,
            HttpServletRequest request) {
        if (!validateSession(request)) {
            // haven't signed in
            Utility.addErrorMessage(model, "YOU HAVE NOT SIGNED IN YET");
            return "login";
        }
        deleteSession(request);
        return "redirect:/login.html";
    }
}
