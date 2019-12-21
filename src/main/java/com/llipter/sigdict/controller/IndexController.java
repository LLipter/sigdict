package com.llipter.sigdict.controller;

import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends SessionController {

    @GetMapping(value = {"/", "/index.html"})
    public String getIndexPage(Model model,
                               HttpServletRequest request) {
        User user = validateSession(request);
        if (user != null)
            PassMessage.addUserMessage(model, user);
        return "index";
    }
}
