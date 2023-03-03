package com.cymark.cymarkdelivery.service.serviceImpl;

import com.cymark.cymarkdelivery.service.EmailService;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "ogbodouchenna1425@gmail.com";
    private static final String SMTP_AUTH_PASSWORD = "vgjribbfzxqfowbb";

    @Override
    public void sendEmail(String recipient, String subject, String message) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(SMTP_HOST_NAME);
            email.setSmtpPort(SMTP_HOST_PORT);
            email.setAuthenticator(new DefaultAuthenticator(SMTP_AUTH_USER, SMTP_AUTH_PASSWORD));
            email.setSSLOnConnect(true);
            email.setFrom(SMTP_AUTH_USER);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(recipient);
            email.send();
            System.out.println("Email sent successfully to " + recipient);
        } catch (Exception e) {
            System.out.println("Failed to send email to " + recipient);
            e.printStackTrace();
        }
    }
}