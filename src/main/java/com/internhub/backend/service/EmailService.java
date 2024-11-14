package com.internhub.backend.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);

    void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;
}
