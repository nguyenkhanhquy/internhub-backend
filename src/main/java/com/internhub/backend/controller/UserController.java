package com.internhub.backend.controller;

import com.internhub.backend.dto.request.user.CreateUserRequest;
import com.internhub.backend.dto.request.user.UpdateUserRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.dto.user.UserDTO;
import com.internhub.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> getUserById(@PathVariable("id") String id) {
        UserDTO user = userService.getUserById(id);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User found")
                .result(user)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserDTO user = userService.createUser(createUserRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User created")
                .result(user)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> updateUser(@Valid @PathVariable("id") String id, @RequestBody UpdateUserRequest updateUserRequest) {
        UserDTO user = userService.updateUser(id, updateUserRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User updated")
                .result(user)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> deleteUser(@PathVariable("id") String id) {
        UserDTO user = userService.deleteUser(id);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User deleted")
                .result(user)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
