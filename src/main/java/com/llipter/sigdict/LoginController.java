package com.llipter.sigdict;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(name = "error_msg", required = false, defaultValue = "") String error_msg, Model model) {
        model.addAttribute("has_error", !error_msg.isEmpty());
        model.addAttribute("error_msg", error_msg);
        return "login";
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String greeting(@RequestParam(name = "username", required = true) String username,
//                           @RequestParam(name = "password", required = true) String password,
//                           Model model) {
//        model.addAttribute("error_msg", "default-error");
//        return "login";
//    }
}