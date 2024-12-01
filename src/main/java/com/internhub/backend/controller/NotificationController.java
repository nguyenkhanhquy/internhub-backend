package com.internhub.backend.controller;

import com.internhub.backend.dto.account.NotificationDTO;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user")
    public ResponseEntity<SuccessResponse<List<NotificationDTO>>> getAllNotificationsByUser() {
        List<NotificationDTO> notifications = notificationService.getAllNotificationsByUser();

        SuccessResponse<List<NotificationDTO>> successResponse = SuccessResponse.<List<NotificationDTO>>builder()
                .result(notifications)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
