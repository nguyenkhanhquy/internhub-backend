package com.internhub.backend.controller;

import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.dto.user.UserDTO;
import com.internhub.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();

        String message = users.isEmpty() ? "No users found" : "Users found";

        SuccessResponse<List<UserDTO>> successResponse = SuccessResponse.<List<UserDTO>>builder()
                .message(message)
                .result(users)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
