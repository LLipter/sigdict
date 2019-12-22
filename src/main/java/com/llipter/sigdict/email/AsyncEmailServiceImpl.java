package com.llipter.sigdict.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncEmailServiceImpl implements AsyncEmailService {
    @Override
    @Async
    public void sendVerificationEmail(String fromUsername, String toEmail, String baseUrl, String token) {
        SyncEmailImpl.sendVerificationEmail(fromUsername, toEmail, baseUrl, token);
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String fromUsername, String toEmail, String baseUrl, String token) {
        SyncEmailImpl.sendResetPasswordEmail(fromUsername, toEmail, baseUrl, token);
    }
}
