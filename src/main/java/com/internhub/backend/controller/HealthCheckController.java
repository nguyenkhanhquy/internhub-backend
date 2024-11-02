package com.internhub.backend.controller;

import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    private static final Map<Integer, String> httpStatusMessages = new HashMap<>();

    static {
        httpStatusMessages.put(200, "OK");
        httpStatusMessages.put(201, "Created");
        httpStatusMessages.put(400, "Bad Request");
        httpStatusMessages.put(401, "Unauthorized");
        httpStatusMessages.put(403, "Forbidden");
        httpStatusMessages.put(404, "Not Found");
        httpStatusMessages.put(500, "Internal Server Error");
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Void>> healthCheck() {
        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Server is running and reachable")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/http/{code}")
    public ResponseEntity<SuccessResponse<Void>> httpCode(@PathVariable("code") int code) {
        String message = httpStatusMessages.getOrDefault(code, "Status code '" + code + "' is not supported");

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .statusCode(code)
                .message(message)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
