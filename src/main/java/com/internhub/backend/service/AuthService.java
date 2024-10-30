package com.internhub.backend.service;

import com.internhub.backend.dto.request.auth.LoginRequest;

import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginRequest loginRequest);
}
