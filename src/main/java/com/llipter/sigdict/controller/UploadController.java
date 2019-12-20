package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UploadController extends SessionController{

    @GetMapping(value = "/upload.html")
    public String getUploadPage(Model model,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        if (!validateSession(request)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name = "file", required = true) MultipartFile file,
                             @RequestParam(name = "encrypted", required = true, defaultValue = "false") boolean encrypted,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        if (!validateSession(request)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }

        System.out.println(file.getName());
        System.out.println(file.getSize());
        System.out.println(file.getOriginalFilename());
        System.out.println(encrypted);

//        storageService.store(file);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/upload.html";
    }

//    @Override
//    public ModelAndView resolveException(HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         Object handler,
//                                         Exception exception) {
//        ModelAndView modelAndView = new ModelAndView("upload");
//        System.out.println("oooo");
//        System.out.println(exception.getClass().getName());
//        if (exception instanceof MaxUploadSizeExceededException) {
////            ModelAndView modelAndView = new ModelAndView("upload");
////            modelAndView.getModel().put("has_error", true);
////            modelAndView.getModel().put("error_msg", ErrorMessage.MAX_FILE_SIZE_EXCEEDED);
////            return modelAndView;
////            Utility.addModelErrorMessage(modelAndView.getModel(), ErrorMessage.MAX_FILE_SIZE_EXCEEDED);
//        }
//        return modelAndView;
//    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleError(MaxUploadSizeExceededException exception,
                              RedirectAttributes redirectAttributes) {
        Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.MAX_FILE_SIZE_EXCEEDED);
        System.out.println("handled");
        return "redirect:/upload.html";
    }
}
