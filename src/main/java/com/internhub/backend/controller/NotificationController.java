package com.internhub.backend.controller;

import com.internhub.backend.dto.account.NotificationDTO;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/mark-as-read/{id}")
    public ResponseEntity<SuccessResponse<Void>> markNotificationAsRead(@PathVariable("id") String id) {
        notificationService.markNotificationAsRead(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Thông báo đã được đánh dấu là đã đọc")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
