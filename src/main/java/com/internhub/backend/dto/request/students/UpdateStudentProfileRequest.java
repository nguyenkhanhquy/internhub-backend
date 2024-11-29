package com.internhub.backend.dto.request.students;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.Major;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateStudentProfileRequest {

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Ngày sinh không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotNull(message = "Ngày tốt nghiệp dự kiến không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expGrad;

    @NotNull(message = "Trạng thái thực tập không được để trống")
    private InternStatus internStatus;

    @NotNull(message = "Chuyên ngành không được để trống")
    private Major major;

    @NotNull(message = "Điểm trung bình không được để trống")
    private double gpa;
}
