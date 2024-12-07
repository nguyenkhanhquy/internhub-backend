package com.internhub.backend.dto.request.jobs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteJobPostRequest {

    @NotBlank(message = "Lý do không được để trống")
    private String reason;
}
