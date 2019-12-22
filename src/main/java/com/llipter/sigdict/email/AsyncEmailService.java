package com.llipter.sigdict.email;

public interface AsyncEmailService {

    void sendVerificationEmail(String fromUsername, String toEmail, String baseUrl, String token);

    void sendResetPasswordEmail(String fromUsername, String toEmail, String baseUrl, String token);
}
