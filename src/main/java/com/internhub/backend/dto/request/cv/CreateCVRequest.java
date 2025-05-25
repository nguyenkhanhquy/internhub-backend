package com.internhub.backend.dto.request.cv;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCVRequest {

    @NotNull(message = "Tiêu đề không được để trống")
    private String title;

    @NotNull(message = "Đường dẫn tệp không được để trống")
    private String filePath;
}
