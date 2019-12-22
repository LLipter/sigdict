package com.llipter.sigdict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {

    @GetMapping(value = "/message.html")
    public String getChangeEmailPage() {
        return "message";
    }

}
