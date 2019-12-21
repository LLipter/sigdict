package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.exception.BadRequestException;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EmailController extends SessionController {

    @GetMapping(value = "/verifyemail")
    public String verifyEmail(HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }

        if (user.isVerified()) {
            throw new BadRequestException(ErrorMessage.EMAIL_ALREADY_VERIFIED);
        }

        PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_SENT);

        return "redirect:/main.html";
    }
}
