package com.internhub.backend.service;

import com.internhub.backend.dto.account.NotificationDTO;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getAllNotificationsByUser();

    void markNotificationAsRead(String notificationId);
}
