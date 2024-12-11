package com.internhub.backend.dto.request.internshipreports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateInternshipReportRequest {

    @NotBlank(message = "Tên công ty không được để trống")
    private String companyName;

    @NotBlank(message = "Tên giảng viên hướng dẫn không được để trống")
    private String teacherName;

    @NotBlank(message = "Tên người hướng dẫn không được để trống")
    private String instructorName;

    @NotBlank(message = "Email người hướng dẫn không được để trống")
    private String instructorEmail;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "File báo cáo không được để trống")
    private String reportFile;

    @NotBlank(message = "File đánh giá không được để trống")
    private String evaluationFile;

    @JsonProperty("isSystemCompany")
    @NotNull(message = "Phải cho biết công ty thuộc hệ thống hay không")
    private boolean isSystemCompany;
}
