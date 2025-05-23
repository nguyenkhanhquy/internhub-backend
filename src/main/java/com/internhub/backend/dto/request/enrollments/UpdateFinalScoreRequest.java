package com.internhub.backend.dto.request.enrollments;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateFinalScoreRequest {

    @NotNull(message = "Điểm không được bỏ trống")
    @Min(value = 0, message = "Điểm phải lớn hơn hoặc bằng 0")
    @Max(value = 10, message = "Điểm phải nhỏ hơn hoặc bằng 10")
    private Double finalScore;

    @NotNull(message = "Nhận xét không được bỏ trống")
    private String feedback;
}
