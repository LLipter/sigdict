package com.llipter.sigdict.test;

import com.llipter.sigdict.utility.EmailHelper;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class EmailTest {
    private static final String username = "sigdict@gmail.com";
    private static final String password = "SIGDICT2020";
    private static final String fromEmail = "no-reply@sigdict.com";
    private static final String toEmail = "llipteran@gmail.com";
    private static final String host = "smtp.gmail.com";


    public static void main(String args[]) throws AddressException, MessagingException {
//        EmailHelper.sendEmail(
//                "llipter@outlook.com",
//                "君のことが大好きだ！",
//                "今でも君はわたしの光だ");
//        EmailHelper.sendEmail(
//                "llipter@outlook.com",
//                "[Action Required] SigDict: Verify your email address",
//                "Click the following link to verify your email address: http://xxxx");

        System.out.println(EmailHelper.getVerificationEmailContent("Ran", "http:/localhost/sdsdsafe"));
        System.out.println(EmailHelper.getResetPasswordEmailContent("Ran", "http:/localhost/sdsdsafe"));


//        EmailHelper.sendEmail(
//                "not-exist@sigdict.com",
//                "君のことが大好きだ！",
//                "今でも君はわたしの光だ");
    }


}


