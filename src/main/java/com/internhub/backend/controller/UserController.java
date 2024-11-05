package com.internhub.backend.controller;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.users.*;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        List<UserDTO> userDTOs = userService.getAllUsers();

        SuccessResponse<List<UserDTO>> successResponse = SuccessResponse.<List<UserDTO>>builder()
                .result(userDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> getUserById(@PathVariable("id") String id) {
        UserDTO userDTO = userService.getUserById(id);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserDTO userDTO = userService.createUser(createUserRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đã tạo người dùng")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> updateUser(@Valid @PathVariable("id") String id, @RequestBody UpdateUserRequest updateUserRequest) {
        UserDTO userDTO = userService.updateUser(id, updateUserRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đã cập nhật người dùng")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> deleteUser(@PathVariable("id") String id) {
        UserDTO userDTO = userService.deleteUser(id);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đã xóa người dùng")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<SuccessResponse<UserDTO>> registerRecruiter(@Valid @RequestBody RegisterRecruiterRequest registerRecruiterRequest) {
        UserDTO userDTO = userService.registerRecruiter(registerRecruiterRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đăng ký thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/register/student")
    public ResponseEntity<SuccessResponse<UserDTO>> registerStudent(@Valid @RequestBody RegisterStudentRequest registerStudentRequest) {
        UserDTO userDTO = userService.registerStudent(registerStudentRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đăng ký thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/verify/send-otp")
    public ResponseEntity<SuccessResponse<Void>> sendOTP(@RequestBody Map<String, String> request) {
        userService.sendOTP(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Đã gửi mã OTP đến email")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/verify/activate-account")
    public ResponseEntity<SuccessResponse<Void>> activateAccount(@RequestBody Map<String, String> request) {
        userService.activateAccount(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Kích hoạt tài khoản thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-password")
    public ResponseEntity<SuccessResponse<Void>> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(updatePasswordRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật mật khẩu thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
