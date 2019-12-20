package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController extends SessionController {

    @PostMapping(path = "/logout")
    public String logout(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        if (!validateSession(request)) {
            // haven't signed in
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }
        deleteSession(request);
        return "redirect:/index.html";
    }
}
