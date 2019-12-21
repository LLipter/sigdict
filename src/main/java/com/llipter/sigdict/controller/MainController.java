package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController extends SessionController {
    @RequestMapping(value = "/main.html", method = RequestMethod.GET)
    public String getMainPage(Model model,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            // haven't signed in
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }
        PassMessage.addUserMessage(model, user);
        return "main";
    }
}
