package com.llipter.sigdict.utility;

import com.llipter.sigdict.ErrorMessage;
import com.llipter.sigdict.exception.InternalServerException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {
    private static final String username = "sigdict@gmail.com";
    private static final String password = "SIGDICT2020";
    private static final String fromEmail = "sigdict@gmail.com";
    private static final String hostServer = "smtp.gmail.com";

    private static final String VERIFICATION_EMAIL_SUBJECT = "[Action Required] SigDict: Verify Your Email Address";
    private static final String RESET_PASSWORD_EMAIL_SUBJECT = "[Action Required] SigDict: Reset Your Password";

    private static String getEmailOpening(String username) {
        return "Dear " + username + ",\n\n";
    }

    private static String getEmailClosing() {
        return "\n\n" + "Best Regards,\n" + "SigDict Team";
    }

    public static String getVerificationEmailContent(String username, String link) {
        StringBuffer sb = new StringBuffer();
        sb.append(getEmailOpening(username));
        sb.append("Please click the following link to verify your email address:\n");
        sb.append(link);
        sb.append(getEmailClosing());
        return sb.toString();
    }

    public static String getResetPasswordEmailContent(String username, String link) {
        StringBuffer sb = new StringBuffer();
        sb.append(getEmailOpening(username));
        sb.append("Please click the following link to reset your password:\n");
        sb.append(link);
        sb.append(getEmailClosing());
        return sb.toString();
    }

    public static void sendEmail(String toEmail, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostServer);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(content);

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            throw new InternalServerException(ErrorMessage.CANNOT_SEND_EMAIL, e);
        }
    }
}
