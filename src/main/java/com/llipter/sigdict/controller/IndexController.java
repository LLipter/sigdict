package com.llipter.sigdict.controller;

import com.llipter.sigdict.Utility;
import com.llipter.sigdict.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends SessionController {

    @GetMapping(value = {"/", "/index.html"})
    public String getLoginPage(Model model,
                               HttpServletRequest request) {
        if (validateSession(request)) {
            User user = getUserFromSession(request);
            Utility.addUserMessage(model, user);
        }
        return "index";
    }
}
