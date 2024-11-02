package com.internhub.backend.service;

public interface OtpService {

    int generateOtp(String email);

    boolean validateOtp(String email, int otp);
}
