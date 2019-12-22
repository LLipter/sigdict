package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.utility.PassMessage;
import com.llipter.sigdict.utility.ValidateInput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PasswordController extends SessionController {

    @GetMapping(value = "/changepassword.html")
    public String getChangePasswordPage(HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }
        return "changepassword";
    }

    @PostMapping(value = "/changepassword")
    public String changepassword(HttpServletRequest request,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(name = "current", required = true) String passwordCurrent,
                                 @RequestParam(name = "new", required = true) String passwordNew,
                                 @RequestParam(name = "confirm", required = true) String passwordConfirm) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }

        // validate the current password
        if (!user.validatePassword(passwordCurrent)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INCORRECT);
            return "redirect:/changepassword.html";
        }

        // validate the new password
        if (!ValidateInput.isValidPassword(passwordNew)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INVALID);
            return "redirect:/changepassword.html";
        }

        // validate retyped new password
        if (!passwordConfirm.equals(passwordNew)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_MISMATCHED);
            return "redirect:/changepassword.html";
        }

        // change password
        user.changePassword(passwordNew);
        userRepository.save(user);

        return "redirect:/main.html";
    }
}
