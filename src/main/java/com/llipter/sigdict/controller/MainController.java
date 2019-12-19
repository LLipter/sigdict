package com.llipter.sigdict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    @RequestMapping(value = "/main.html", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "main";
    }
}
