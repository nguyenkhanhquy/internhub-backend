package com.internhub.backend.mapper;

import com.internhub.backend.dto.user.UserDTO;
import com.internhub.backend.entity.User.Role;
import com.internhub.backend.entity.User.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    UserDTO mapUserToUserDTO(User user);

    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.getName() : null;
    }
}
