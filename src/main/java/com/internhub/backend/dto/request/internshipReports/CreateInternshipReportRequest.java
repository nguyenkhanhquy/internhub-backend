package com.internhub.backend.dto.request.internshipReports;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String intructorName;

    @NotBlank(message = "Email người hướng dẫn không được để trống")
    private String intructorEmail;

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
}
