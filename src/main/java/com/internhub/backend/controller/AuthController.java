package com.internhub.backend.controller;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.auth.IntrospectRequest;
import com.internhub.backend.dto.request.auth.LoginRequest;
import com.internhub.backend.dto.request.auth.LogoutRequest;
import com.internhub.backend.dto.request.auth.RegisterStudentRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .message("Đăng nhập thành công")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> introspect(@RequestBody IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        Map<String, Object> resultData = authService.introspect(introspectRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Token hợp lệ")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Đăng xuất thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserDTO>> getCurrentAuthUser() {
        UserDTO userDTO = authService.getCurrentAuthUser();

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/register/student")
    public ResponseEntity<SuccessResponse<UserDTO>> registerStudent(@Valid @RequestBody RegisterStudentRequest registerStudentRequest) {
        UserDTO userDTO = authService.registerStudent(registerStudentRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đăng ký thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
