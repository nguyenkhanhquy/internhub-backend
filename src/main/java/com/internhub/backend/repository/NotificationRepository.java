package com.internhub.backend.repository;

import com.internhub.backend.entity.account.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {

}
