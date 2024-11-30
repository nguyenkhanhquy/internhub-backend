package com.internhub.backend.dto.request.jobs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.internhub.backend.entity.student.Major;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class JobPostUpdateRequest {

    @NotBlank(message = "Tiêu đề không được bỏ trống")
    private String title;

    @NotBlank(message = "Vị trí công việc không được bỏ trống")
    private String jobPosition;

    @NotNull(message = "Số lượng tuyển không được bỏ trống")
    @Min(value = 1, message = "Số lượng tuyển phải lớn hơn hoặc bằng 1")
    private Integer quantity;

    @NotBlank(message = "Lương không được bỏ trống")
    private String salary;

    @NotBlank(message = "Loại công việc không được bỏ trống")
    private String type;

    @NotBlank(message = "Hình thức làm việc không được bỏ trống")
    private String remote;

    @NotNull(message = "Danh sách ngành không được bỏ trống")
    @Size(min = 1, message = "Danh sách ngành không được rỗng")
    private List<Major> majors;

    @NotNull(message = "Ngày hết hạn không được bỏ trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    @NotBlank(message = "Địa chỉ làm việc không được bỏ trống")
    private String address;

    @NotBlank(message = "Mô tả công việc không được bỏ trống")
    private String description;

    @NotBlank(message = "Yêu cầu công việc không được bỏ trống")
    private String requirements;

    @NotBlank(message = "Quyền lợi không được bỏ trống")
    private String benefits;
}
