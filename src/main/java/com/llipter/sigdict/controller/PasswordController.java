package com.llipter.sigdict.controller;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.email.AsyncEmailService;
import com.llipter.sigdict.entity.ResetPasswordToken;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.exception.BadRequestException;
import com.llipter.sigdict.repository.ResetPasswordTokenRepository;
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
public class PasswordController extends SessionController {

    @Autowired
    protected ResetPasswordTokenRepository resetPasswordTokenRepository;

    private final AsyncEmailService asyncEmailService;

    @Autowired
    public PasswordController(AsyncEmailService asyncEmailService) {
        this.asyncEmailService = asyncEmailService;
    }


    @GetMapping(value = "/changepassword.html")
    public String getChangePasswordPage(HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }
        return "changepassword";
    }

    @PostMapping(value = "/changepassword")
    public String changePassword(HttpServletRequest request,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(name = "current", required = true) String passwordCurrent,
                                 @RequestParam(name = "new", required = true) String passwordNew,
                                 @RequestParam(name = "confirm", required = true) String passwordConfirm) {
        User user = validateSession(request);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.NOT_SIGN_IN_YET);
            return "redirect:/login.html";
        }

        // validate the current password
        if (!user.validatePassword(passwordCurrent)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INCORRECT);
            return "redirect:/changepassword.html";
        }

        // validate the new password
        if (!ValidateInput.isValidPassword(passwordNew)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INVALID);
            return "redirect:/changepassword.html";
        }

        // validate retyped new password
        if (!passwordConfirm.equals(passwordNew)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_MISMATCHED);
            return "redirect:/changepassword.html";
        }

        // change password
        user.changePassword(passwordNew);
        userRepository.save(user);

        return "redirect:/main.html";
    }

    @GetMapping(value = "/forgetpassword.html")
    public String getForgetPasswordPage() {
        return "forgetpassword";
    }

    @PostMapping(value = "/sendresetpasswordlink")
    public String sendResetPasswordLink(HttpServletRequest request,
                                        RedirectAttributes redirectAttributes,
                                        @RequestParam(name = "username", required = true) String username,
                                        @RequestParam(name = "email", required = true) String email) {
        // validate username
        if (!ValidateInput.isValidUsername(username)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.USERNAME_INVALID);
            return "redirect:/forgetpassword.html";
        }

        // validate email
        if (!ValidateInput.isValidEmail(email)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_INVALID);
            return "redirect:/forgetpassword.html";
        }

        // username incorrect
        User user = userRepository.findByUsername(username);
        if (user == null) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.USER_NOT_EXISTED);
            return "redirect:/forgetpassword.html";
        }

        // username not match with email
        if (!user.getEmail().equals(email)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_INCORRECT);
            return "redirect:/forgetpassword.html";
        }

        // email not verified
        if (!user.isVerified()) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.EMAIL_NOT_VERIFIED);
            return "redirect:/forgetpassword.html";
        }

        // invalidate existing token
        ResetPasswordToken resetPasswordToken = user.getResetPasswordToken();
        if (resetPasswordToken != null) {
            deleteResetPasswordToken(resetPasswordToken);
        }

        // generate new token
        resetPasswordToken = new ResetPasswordToken(user);
        userRepository.save(user);

        // send reset email
        asyncEmailService.sendResetPasswordEmail(
                username,
                email,
                Utility.getBaseUrlFromRequest(request),
                resetPasswordToken.getToken());

        PassMessage.addRedirectAttributesMessage(redirectAttributes, ErrorMessage.RESET_LINK_SENT);
        return "redirect:/message.html";
    }

    private void deleteResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        User user = resetPasswordToken.getUser();
        resetPasswordToken.setUser(null);
        user.setResetPasswordToken(null);
        userRepository.save(user);
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }

    private User isTokenValid(String token, Model model) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token);
        if (resetPasswordToken == null) {
            // malicious request
            throw new BadRequestException(ErrorMessage.TOKEN_INVALID);
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.after(resetPasswordToken.getValidThrough())) {
            // delete expired password token
            deleteResetPasswordToken(resetPasswordToken);
            PassMessage.addModelErrorPageMessage(model,
                    ErrorMessage.LINK_EXPIRED,
                    ErrorMessage.LINK_EXPIRED_MESSAGE);
            return null;
        }

        return resetPasswordToken.getUser();
    }

    @GetMapping(value = "/resetpassword.html")
    public String getResetPasswordPage(Model model,
                                       RedirectAttributes redirectAttributes,
                                       @RequestParam(name = "token", required = true) String token) {
        if (isTokenValid(token, model) == null)
            return "error";

        PassMessage.addModelToken(model, token);
        return "resetpassword";
    }

    @PostMapping(value = "/resetpassword")
    public String resetPassword(Model model,
                                RedirectAttributes redirectAttributes,
                                @RequestParam(name = "token", required = true) String token,
                                @RequestParam(name = "password", required = true) String password,
                                @RequestParam(name = "confirm", required = true) String confirm) {
        System.out.println(token);
        User user = isTokenValid(token, model);
        if (user == null)
            return "error";

        // validate the new password
        if (!ValidateInput.isValidPassword(password)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_INVALID);
            PassMessage.addRedirectAttributesToken(redirectAttributes, token);
            return "redirect:/resetpassword.html?token=" + Utility.urlEncodedString(token);
        }

        // validate retyped new password
        if (!password.equals(confirm)) {
            PassMessage.addRedirectAttributesErrorMessage(redirectAttributes, ErrorMessage.PASSWORD_MISMATCHED);
            PassMessage.addRedirectAttributesToken(redirectAttributes, token);
            return "redirect:/resetpassword.html?token=" + Utility.urlEncodedString(token);
        }

        // reset password
        user.changePassword(password);
        // password token is no longer needed
        deleteResetPasswordToken(user.getResetPasswordToken());

        PassMessage.addRedirectAttributesMessage(redirectAttributes, ErrorMessage.PASSWORD_RESET);
        return "redirect:/message.html";
    }
}
