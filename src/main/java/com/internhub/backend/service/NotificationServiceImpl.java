package com.internhub.backend.service;

import com.internhub.backend.dto.account.NotificationDTO;
import com.internhub.backend.entity.account.Notification;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.NotificationMapper;
import com.internhub.backend.repository.NotificationRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final WebSocketService webSocketService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDTO> getAllNotificationsByUser() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        return notificationRepository.findAllByUser(user).stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    @Override
    public void markNotificationAsRead(String notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    @Override
    public void sendNotification(User user, String title, String content) {
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();

        user.getNotifications().add(notification);
        userRepository.save(user);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public void sendNotificationByUserId(String userId, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));
        sendNotification(user, title, content);
    }
}
