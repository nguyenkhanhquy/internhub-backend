package com.internhub.backend.service;

import com.internhub.backend.dto.request.user.CreateUserRequest;
import com.internhub.backend.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO createUser(CreateUserRequest createUserRequest);
}
