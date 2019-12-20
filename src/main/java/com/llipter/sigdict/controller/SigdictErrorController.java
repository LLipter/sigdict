package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.Utility;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SigdictErrorController implements ErrorController {


    @Override
    public String getErrorPath() {
        return "${server.error.path}";
    }

    @RequestMapping("${server.error.path}")
    public String handleError(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {

        int status = response.getStatus();
//        System.out.println(status);
        if (status == HttpStatus.NOT_FOUND.value()) {
            Utility.addModelErrorPageMessage(model,
                    HttpStatus.NOT_FOUND.value() + " " +
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ErrorMessage.NOT_FOUND);
        } else if (status == HttpStatus.METHOD_NOT_ALLOWED.value()) {
            Utility.addModelErrorPageMessage(model,
                    HttpStatus.METHOD_NOT_ALLOWED.value() + " " +
                            HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                    ErrorMessage.METHOD_NOT_ALLOWED);
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            Utility.addModelErrorPageMessage(model,
                    HttpStatus.INTERNAL_SERVER_ERROR.value() + " " +
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ErrorMessage.INTERNAL_SERVER_ERROR);
        } else {
            Utility.addModelErrorPageMessage(model,
                    "UNKNOWN ERROR",
                    ErrorMessage.UNKNOWN_ERROR);
        }

        return "error";
    }
}
