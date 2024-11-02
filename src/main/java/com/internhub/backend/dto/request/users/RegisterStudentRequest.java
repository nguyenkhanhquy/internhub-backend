package com.internhub.backend.dto.request.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Date;

@Getter
public class RegisterStudentRequest {

    @NotBlank(message = "Email không được bỏ trống")
    @Email(message = "Định dạng email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Tên sinh viên không được bỏ trống")
    private String name;

    @NotNull(message = "Giới tính không được bỏ trống")
    private boolean gender;

    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phone;

    @NotBlank(message = "Địa chỉ không được bỏ trống")
    private String address;

    @NotNull(message = "Ngày sinh không được bỏ trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;

    @NotNull(message = "Ngày tốt nghiệp dự kiến không được bỏ trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expGrad;

    @NotBlank(message = "Chuyên ngành không được bỏ trống")
    private String major;

    @NotBlank(message = "Tình trạng thực tập không được bỏ trống")
    private String internStatus;

    @NotBlank(message = "Mã sinh viên không được bỏ trống")
    private String studentId;

    @NotNull(message = "GPA không được bỏ trống")
    private double gpa;
}
