package com.internhub.backend.service;

import com.internhub.backend.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();
}
