package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.utility.PassMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UploadController extends SessionController {

    @GetMapping(value = "/upload.html")
    public String getUploadPage(Model model,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        if (!validateSession(request)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
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
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
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


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleError(MaxUploadSizeExceededException exception,
                              RedirectAttributes redirectAttributes) {
        PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.MAX_FILE_SIZE_EXCEEDED);
        System.out.println("handled");
        return "redirect:/upload.html";
    }
}
