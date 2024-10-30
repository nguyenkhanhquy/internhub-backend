package com.internhub.backend.controller;

import com.internhub.backend.dto.request.auth.IntrospectRequest;
import com.internhub.backend.dto.request.auth.LoginRequest;
import com.internhub.backend.dto.request.auth.LogoutRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> resultData = authService.login(loginRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Login successfully")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> introspect(@RequestBody IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        Map<String, Object> resultData = authService.introspect(introspectRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Token is valid")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Logout successfully")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
