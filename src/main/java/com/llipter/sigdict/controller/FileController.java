package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.entity.UploadedFile;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.exception.BadRequestException;
import com.llipter.sigdict.exception.InternalServerException;
import com.llipter.sigdict.security.SignatureType;
import com.llipter.sigdict.security.SymmetricEncryption;
import com.llipter.sigdict.storage.StorageService;
import com.llipter.sigdict.utility.PassMessage;
import com.llipter.sigdict.utility.ValidateInput;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

@Controller
public class FileController extends SessionController {

    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/upload.html")
    public String getUploadPage(HttpServletRequest request,
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
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }

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


        try {
            String filename = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            byte[] data = IOUtils.toByteArray(inputStream);
            PrivateKey privateKey = null;
            if (signatureType == SignatureType.DSA)
                privateKey = user.getUnencryptedDsaPrivateKey();
            else
                privateKey = user.getUnencryptedRsaPrivateKey();
            UploadedFile uploadedFile = new UploadedFile(filename, data, signatureType, privateKey, encrypted);
            user.getUploadedFiles().add(uploadedFile);
            userRepository.save(user);
            if (encrypted) {
                data = SymmetricEncryption.encrypt(user.getUnencryptedUserEncryptionKey(), data);
            }
            storageService.store(data, uploadedFile.getIdentifier());
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorMessage.CANNOT_SAVE_FILE, e);
        }

        return "redirect:/main.html";
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception,
                                                       RedirectAttributes redirectAttributes) {
        PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.MAX_FILE_SIZE_EXCEEDED);
        return "redirect:/upload.html";
    }

    @GetMapping(value = "/delete")
    public String deleteFile(HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(name = "identifier", required = true) String fileIdentifier) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }

        UploadedFile uploadedFile = user.getUploadedFileByIdentifier(fileIdentifier);
        if (uploadedFile == null) {
            // malicious request
            throw new BadRequestException(ErrorMessage.FILE_IDENTIFIER_INVALID);
        }

        // delete file from server disk
        storageService.remove(fileIdentifier);

        // delete record in database
        user.removeUploadedFileByIdentifier(fileIdentifier);
        userRepository.save(user);

        return "redirect:/main.html";
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request,
                                                 @RequestParam(name = "identifier", required = true) String fileIdentifier) {

        User user = validateSession(request);
        if (user == null) {
            // malicious request
            throw new BadRequestException(ErrorMessage.SIGH_IN_FIRST);
        }

        UploadedFile uploadedFile = user.getUploadedFileByIdentifier(fileIdentifier);
        if (uploadedFile == null) {
            // malicious request
            throw new BadRequestException(ErrorMessage.FILE_IDENTIFIER_INVALID);
        }

        Resource file = null;
        if (uploadedFile.isEncrypted()) {
            file = storageService.loadEncryptedAsResource(fileIdentifier, user.getUnencryptedUserEncryptionKey());
        } else {
            file = storageService.loadUnencryptedAsResource(fileIdentifier);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + uploadedFile.getFilename() + "\"").body(file);
    }

    @GetMapping(value = "/detail")
    public String getDetailPage(HttpServletRequest request,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                @RequestParam(name = "identifier", required = true) String fileIdentifier) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.SIGH_IN_FIRST);
            return "redirect:/login.html";
        }

        UploadedFile uploadedFile = user.getUploadedFileByIdentifier(fileIdentifier);
        if (uploadedFile == null) {
            // malicious request
            throw new BadRequestException(ErrorMessage.FILE_IDENTIFIER_INVALID);
        }

        PassMessage.addModelDetailPageMessage(model, uploadedFile);
        return "detail";
    }

}
