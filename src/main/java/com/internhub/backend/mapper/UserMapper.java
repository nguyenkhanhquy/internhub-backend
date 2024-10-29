package com.internhub.backend.mapper;

import com.internhub.backend.dto.user.UserDTO;
import com.internhub.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);
}
