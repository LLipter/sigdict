package com.llipter.sigdict.utility;

import com.llipter.sigdict.entity.MainPageFile;
import com.llipter.sigdict.entity.UploadedFile;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.security.SignatureType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

public class PassMessage {
    public static void addModelErrorMessage(Model model, String errorMessage) {
        model.addAttribute("has_error", true);
        model.addAttribute("error_msg", errorMessage);
    }

    public static void addModelErrorPageMessage(Model model, String errorTitle, String errorMessage) {
        model.addAttribute("error_title", errorTitle);
        model.addAttribute("error_msg", errorMessage);
    }

    public static void addModelDetailPageMessage(Model model, UploadedFile uploadedFile) {
        model.addAttribute("identifier", uploadedFile.getIdentifier());
        model.addAttribute("filename", uploadedFile.getFilename());
        model.addAttribute("date", Utility.timestamp2String(uploadedFile.getUploadTime()));
        String sigType = null;
        if (uploadedFile.getSignatureType() == SignatureType.DSA)
            sigType = "DSA";
        else
            sigType = "RSA";
        model.addAttribute("signature_type", sigType);
        model.addAttribute("signature", Utility.binary2base64(uploadedFile.getSignature()));
    }

    public static void addModelMainPageFiles(Model model, User user) {
        List<MainPageFile> mainPageFiles = new ArrayList<MainPageFile>();
        List<UploadedFile> uploadedFiles = user.getUploadedFiles();
        for (UploadedFile uploadedFile : uploadedFiles) {
            MainPageFile mainPageFile = new MainPageFile();
            mainPageFile.setFilename(uploadedFile.getFilename());
            mainPageFile.setIdentifier(Utility.urlEncodedString(uploadedFile.getIdentifier()));
            mainPageFile.setDate(Utility.timestamp2String(uploadedFile.getUploadTime()));
            mainPageFiles.add(mainPageFile);
        }
        model.addAttribute("files", mainPageFiles);
        model.addAttribute("file_number", mainPageFiles.size());
    }

    public static void addRedirectAttributesErrorMessage(RedirectAttributes redirectAttributes, String errorMessage) {
        redirectAttributes.addFlashAttribute("has_error", true);
        redirectAttributes.addFlashAttribute("error_msg", errorMessage);
    }

    public static void addRedirectAttributesMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("has_msg", true);
        redirectAttributes.addFlashAttribute("msg", message);
    }

    public static void addRedirectAttributesToken(RedirectAttributes redirectAttributes, String token) {
        redirectAttributes.addFlashAttribute("token", token);
    }

    public static void addModelToken(Model model, String token) {
        model.addAttribute("token", token);
    }

    public static void addUserMessage(Model model, User user) {
        model.addAttribute("has_signed_in", true);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("email_verified", user.isVerified());
        model.addAttribute("dsa_pubkey", Utility.binary2base64(user.getDsaPublicKey()));
        model.addAttribute("rsa_pubkey", Utility.binary2base64(user.getRsaPublicKey()));
    }
}
