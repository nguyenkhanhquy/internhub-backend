package com.internhub.backend.service;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.user.CreateUserRequest;
import com.internhub.backend.dto.request.user.UpdateUserRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    List<UserDTO> getAllUsers();

    @PostAuthorize("returnObject.email == authentication.name or hasAuthority('SCOPE_FIT')")
    UserDTO getUserById(String id);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    UserDTO createUser(CreateUserRequest createUserRequest);

    @PostAuthorize("returnObject.email == authentication.name or hasAuthority('SCOPE_FIT')")
    UserDTO updateUser(String id, UpdateUserRequest updateUserRequest);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    UserDTO deleteUser(String id);
}
