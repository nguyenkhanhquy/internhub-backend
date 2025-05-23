package com.internhub.backend.service;

import com.internhub.backend.dto.account.NotificationDTO;
import com.internhub.backend.entity.account.User;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getAllNotificationsByUser();

    void markNotificationAsRead(String notificationId);

    void sendNotification(User user, String title, String content);

    void sendNotificationByUserId(String userId, String title, String content);
}
