package com.internhub.backend.dto.request.teachers;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TeacherUpdateRequest {

    @NotBlank(message = "Tên được bỏ trống")
    private String name;

    @NotBlank(message = "Mã giảng viên được bỏ trống")
    private String teacherId;
}
