package com.llipter.sigdict.utility;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.MainPageFile;
import com.llipter.sigdict.entity.UploadedFile;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.exception.InternalServerException;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public static void addModelMainPageFiles(Model model, User user) {
        List<MainPageFile> mainPageFiles = new ArrayList<MainPageFile>();
        List<UploadedFile> uploadedFiles = user.getUploadedFiles();
        for (UploadedFile uploadedFile : uploadedFiles) {
            MainPageFile mainPageFile = new MainPageFile();
            mainPageFile.setFilename(uploadedFile.getFilename());
            try {
                // The identifier will appear in user's main page
                // and will be used to construct a url
                // thus this encoding will prevent special characters like "+" from being misinterpreted.
                mainPageFile.setIdentifier(
                        URLEncoder.encode(uploadedFile.getIdentifier(),
                                StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new InternalServerException(ErrorMessage.CANNOT_ENCODE, e);
            }
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

    public static void addUserMessage(Model model, User user) {
        model.addAttribute("has_signed_in", true);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("dsa_pubkey", Utility.binary2base64(user.getDsaPublicKey()));
        model.addAttribute("rsa_pubkey", Utility.binary2base64(user.getRsaPublicKey()));
    }
}
