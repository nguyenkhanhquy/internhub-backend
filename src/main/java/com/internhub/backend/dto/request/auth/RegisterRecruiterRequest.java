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

    @NotBlank(message = "Password không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Company không được bỏ trống")
    private String company;

    @NotBlank(message = "Recruiter không được bỏ trống")
    private String recruiterName;

    @NotBlank(message = "Position không được bỏ trống")
    private String position;

    @NotBlank(message = "Phone không được bỏ trống")
    private String phone;

    @NotBlank(message = "Recruiter email không được bỏ trống")
    private String recruiterEmail;
}
