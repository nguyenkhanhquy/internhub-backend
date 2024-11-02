package com.internhub.backend.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    @NotBlank(message = "Mật khẩu cũ không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu cũ phải có ít nhất 8 ký tự")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu mới phải có ít nhất 8 ký tự")
    private String newPassword;
}
