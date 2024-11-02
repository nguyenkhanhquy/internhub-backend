package com.internhub.backend.controller;

import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthCheckController {

    @RequestMapping
    public ResponseEntity<SuccessResponse<Void>> healthCheck() {
        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Server is running and reachable")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
