package com.llipter.sigdict.utility;

import com.llipter.sigdict.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class PassMessage {
    public static void addModelErrorMessage(Model model, String errorMessage) {
        model.addAttribute("has_error", true);
        model.addAttribute("error_msg", errorMessage);
    }

    public static void addModelErrorPageMessage(Model model, String errorTitle, String errorMessage) {
        model.addAttribute("error_title", errorTitle);
        model.addAttribute("error_msg", errorMessage);
    }

    public static void addRedirectAttributesErrorMessage(RedirectAttributes redirectAttributes, String errorMessage) {
        redirectAttributes.addFlashAttribute("has_error", true);
        redirectAttributes.addFlashAttribute("error_msg", errorMessage);
    }

    public static void addUserMessage(Model model, User user) {
        model.addAttribute("has_signed_in", true);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("pubkey", Utility.binary2base64(user.getDsaPublicKey()));
    }
}
