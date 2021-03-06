package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class SigdictErrorController implements ErrorController {


    @Override
    public String getErrorPath() {
        return "${server.error.path}";
    }

    @RequestMapping("${server.error.path}")
    public String handleError(HttpServletResponse response,
                              Model model) {

        int status = response.getStatus();
//        System.out.println(status);
        if (status == HttpStatus.BAD_REQUEST.value()) {
            PassMessage.addModelErrorPageMessage(model,
                    HttpStatus.BAD_REQUEST.value() + " " +
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ErrorMessage.BAD_REQUEST);
        } else if (status == HttpStatus.NOT_FOUND.value()) {
            PassMessage.addModelErrorPageMessage(model,
                    HttpStatus.NOT_FOUND.value() + " " +
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ErrorMessage.NOT_FOUND);
        } else if (status == HttpStatus.METHOD_NOT_ALLOWED.value()) {
            PassMessage.addModelErrorPageMessage(model,
                    HttpStatus.METHOD_NOT_ALLOWED.value() + " " +
                            HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                    ErrorMessage.METHOD_NOT_ALLOWED);
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            PassMessage.addModelErrorPageMessage(model,
                    HttpStatus.INTERNAL_SERVER_ERROR.value() + " " +
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ErrorMessage.INTERNAL_SERVER_ERROR);
        } else {
            PassMessage.addModelErrorPageMessage(model,
                    "UNKNOWN ERROR",
                    ErrorMessage.UNKNOWN_ERROR);
        }

        return "error";
    }
}
