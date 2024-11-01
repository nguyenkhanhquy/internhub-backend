package com.internhub.backend.mapper;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.entity.account.Role;
import com.internhub.backend.entity.account.User;
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
