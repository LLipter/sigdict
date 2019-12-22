package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class KeyController extends SessionController {

    @PostMapping(value = "/changekey")
    public String changeKey(HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }

        user.changeKey();
        userRepository.save(user);

        PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.KEY_CHANGED);
        return "redirect:/main.html";
    }
}
