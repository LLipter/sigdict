package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.Session;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController extends SessionController {


    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String getLoginPage(HttpServletRequest request) {
        if (validateSession(request) != null)
            return "redirect:/main.html";
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(name = "username", required = true) String username,
                        @RequestParam(name = "password", required = true) String password,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {

        if (validateSession(request) != null) {
            // user already signed in
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.ALREADY_SIGNED_IN);
            return "redirect:/login.html";
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.USER_NOT_EXISTED);
            return "redirect:/login.html";
        } else if (!user.validatePassword(password)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INCORRECT);
            return "redirect:/login.html";
        }

        // login successfully
        // set new session
        user.setSession(new Session(user));
        // set cookie
        Session.setSessionCookie(response, user.getSession().getSessionId());
        userRepository.save(user);

        return "redirect:/main.html";
    }
}