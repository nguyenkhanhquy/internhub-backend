package com.internhub.backend.service;

import com.internhub.backend.dto.account.NotificationDTO;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

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
}
