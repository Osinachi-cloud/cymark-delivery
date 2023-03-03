package com.cymark.cymarkdelivery.service;

public interface EmailService {
    void sendEmail(String recipient, String subject, String message);
}
