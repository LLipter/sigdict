package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.email.AsyncEmailService;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.entity.VerificationToken;
import com.llipter.sigdict.exception.BadRequestException;
import com.llipter.sigdict.repository.VerificationTokenRepository;
import com.llipter.sigdict.utility.PassMessage;
import com.llipter.sigdict.utility.Utility;
import com.llipter.sigdict.utility.ValidateInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Controller
public class EmailController extends SessionController {

    @Autowired
    protected VerificationTokenRepository verificationTokenRepository;

    private final AsyncEmailService asyncEmailService;

    @Autowired
    public EmailController(AsyncEmailService asyncEmailService) {
        this.asyncEmailService = asyncEmailService;
    }

    @GetMapping(value = "/verifyemail")
    public String verifyEmail(HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }

        if (user.isVerified()) {
            throw new BadRequestException(ErrorMessage.EMAIL_ALREADY_VERIFIED);
        }

        // get token
        VerificationToken token = null;
        if (user.getVerificationToken() == null) {
            // create token for the first time
            token = new VerificationToken(user);
        } else {
            // refresh token otherwise
            token = user.getVerificationToken();
            token.setValidThrough(VerificationToken.getRefreshedValidThrough());
        }
        userRepository.save(user);

        // send email
        asyncEmailService.sendVerificationEmail(
                user.getUsername(),
                user.getEmail(),
                Utility.getBaseUrlFromRequest(request),
                user.getVerificationToken().getToken());

        PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_SENT);
        return "redirect:/main.html";
    }

    @GetMapping(value = "/verified")
    public String verifyEmail(Model model,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(name = "token", required = true) String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            // malicious request
            throw new BadRequestException(ErrorMessage.TOKEN_INVALID);
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.after(verificationToken.getValidThrough())) {
            // delete expired verification token
            deleteVerificationToken(verificationToken);
            PassMessage.addModelErrorPageMessage(model,
                    ErrorMessage.LINK_EXPIRED,
                    ErrorMessage.LINK_EXPIRED_MESSAGE);
            return "error";
        }

        // email is verified
        verificationToken.getUser().setVerified(true);
        // verification token is no longer needed
        deleteVerificationToken(verificationToken);

        // send user back to index
        return "redirect:/index.html";
    }

    private void deleteVerificationToken(VerificationToken verificationtoken) {
        User user = verificationtoken.getUser();
        verificationtoken.setUser(null);
        user.setVerificationToken(null);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationtoken);
    }

    @GetMapping(value = "/changeemail.html")
    public String getChangeEmailPage(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }
        return "changeemail";
    }

    @PostMapping(value = "/changeemail")
    public String changeEmail(HttpServletRequest request,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(name = "email", required = true) String email) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }

        // validate email address
        if (!ValidateInput.isValidEmail(email)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_INVALID);
            return "redirect:/changeemail.html";
        }

        // if the user has tried to verify his email address
        // invalidate this operation
        VerificationToken verificationToken = user.getVerificationToken();
        if (verificationToken != null) {
            deleteVerificationToken(verificationToken);
        }

        // change email
        user.changeEmail(email);
        userRepository.save(user);

        PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_CHANGED);
        return "redirect:/main.html";
    }
}
