package com.internhub.backend.dto.request.jobs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.internhub.backend.entity.student.Major;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class CreateJobPostRequest {

    @NotBlank(message = "Tiêu đề không được bỏ trống")
    private String title;

    @NotBlank(message = "Loại công việc không được bỏ trống")
    private String type;

    @NotBlank(message = "Hình thức làm việc không được bỏ trống")
    private String remote;

    @NotBlank(message = "Mô tả công việc không được bỏ trống")
    private String description;

    @NotBlank(message = "Lương không được bỏ trống")
    private String salary;

    @NotNull(message = "Số lượng tuyển không được bỏ trống")
    @Min(value = 1, message = "Số lượng tuyển phải lớn hơn hoặc bằng 1")
    private Integer quantity;

    @NotNull(message = "Ngày hết hạn không được bỏ trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expiryDate;

    @NotBlank(message = "Vị trí công việc không được bỏ trống")
    private String jobPosition;

    @NotBlank(message = "Yêu cầu công việc không được bỏ trống")
    private String requirements;

    @NotBlank(message = "Quyền lợi không được bỏ trống")
    private String benefits;

    @NotBlank(message = "Địa chỉ không được bỏ trống")
    private String address;

    @NotNull(message = "Danh sách ngành không được bỏ trống")
    @Size(min = 1, message = "Danh sách ngành không được rỗng")
    private List<Major> majors;
}
