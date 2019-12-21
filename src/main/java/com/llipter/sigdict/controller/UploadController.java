package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.security.SignatureType;
import com.llipter.sigdict.utility.PassMessage;
import com.llipter.sigdict.utility.ValidateInput;
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
        if (validateSession(request) == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(name = "file", required = true) MultipartFile file,
                             @RequestParam(name = "encrypted", required = true, defaultValue = "false") boolean encrypted,
                             @RequestParam(name = "algorithm", required = true) String algorithm,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        if (validateSession(request) == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }

//        System.out.println("algorithm");
//        System.out.println(algorithm);

        if (file.isEmpty()) {
            // file is missing
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.FILE_NOT_SELECT);
            return "redirect:/upload.html";
        }

        if (!ValidateInput.isValidFilename(file.getOriginalFilename())) {
            // malicious filename
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.FILENAME_INVALID);
            return "redirect:/upload.html";
        }

        if (!ValidateInput.hasValidExtention(file.getOriginalFilename())) {
            // invalid extension
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.FILE_EXTENSION_INVALID);
            return "redirect:/upload.html";
        }

        SignatureType signatureType = null;
        if (algorithm.equals("DSA")) {
            signatureType = SignatureType.DSA;
        } else if (algorithm.equals("RSA")) {
            signatureType = SignatureType.RSA;
        } else {
            // invalid algorithm
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGNATURE_ALGORITHM_INVALID);
            return "redirect:/upload.html";
        }


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
