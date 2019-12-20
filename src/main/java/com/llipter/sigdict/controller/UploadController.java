package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name = "file", required = true) MultipartFile file,
                             @RequestParam(name = "username", required = true) boolean encrypted,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        if (!validateSession(request)) {
            Utility.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }
//        storageService.store(file);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
