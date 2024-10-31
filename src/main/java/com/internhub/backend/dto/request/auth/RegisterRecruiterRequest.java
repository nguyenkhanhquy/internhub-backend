package com.internhub.backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRecruiterRequest {

    @NotBlank(message = "Email không được bỏ trống")
    @Email(message = "Định dạng email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Công ty không được bỏ trống")
    private String company;

    @NotBlank(message = "Tên nhà tuyển dụng không được bỏ trống")
    private String recruiterName;

    @NotBlank(message = "Position không được bỏ trống")
    private String position;

    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phone;

    @NotBlank(message = "Email nhà tuyển dụng không được bỏ trống")
    private String recruiterEmail;
}
