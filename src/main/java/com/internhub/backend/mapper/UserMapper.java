package com.internhub.backend.mapper;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.entity.account.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserDTO mapUserToUserDTO(User user);
}
