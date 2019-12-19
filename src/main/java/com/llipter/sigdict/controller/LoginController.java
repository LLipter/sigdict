package com.llipter.sigdict.controller;

import com.llipter.sigdict.entity.Session;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;


    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String loginPage(@RequestParam(name = "error_msg", required = false, defaultValue = "") String error_msg, Model model) {
        model.addAttribute("has_error", !error_msg.isEmpty());
        model.addAttribute("error_msg", error_msg);
        return "login";
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    public String login(@RequestParam(name = "username", required = true) String username,
                        @RequestParam(name = "password", required = true) String password,
                        Model model) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("has_error", true);
            model.addAttribute("error_msg", "USER NOT EXISTED");
            return "login";
        } else if (!user.validatePassword(password)) {
            model.addAttribute("has_error", true);
            model.addAttribute("error_msg", "PASSWORD INCORRECT");
            return "login";
        }

        // login successfully
        Session session = user.getSession();
        if(session == null){
            // set new session
            user.setSession(new Session(user));
            session = user.getSession();

        }else{
            
        }


        userRepository.save(user);


        return "redirect:/main.html";
    }
}