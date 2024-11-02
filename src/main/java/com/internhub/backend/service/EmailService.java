package com.internhub.backend.service;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);
}
