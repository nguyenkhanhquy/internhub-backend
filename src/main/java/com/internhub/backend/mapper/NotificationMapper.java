package com.internhub.backend.mapper;

import com.internhub.backend.dto.account.NotificationDTO;
import com.internhub.backend.entity.account.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDTO toDTO(Notification notification);
}
