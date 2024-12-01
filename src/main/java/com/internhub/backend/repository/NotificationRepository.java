package com.internhub.backend.repository;

import com.internhub.backend.entity.account.Notification;
import com.internhub.backend.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findAllByUser(User user);
}
