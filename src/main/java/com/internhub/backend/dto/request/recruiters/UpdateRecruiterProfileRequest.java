package com.internhub.backend.dto.request.recruiters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateRecruiterProfileRequest {

    @NotBlank(message = "Tên của nhà tuyển dụng không được để trống")
    private String name;

    @NotBlank(message = "Vị trí của nhà tuyển dụng không được để trống")
    private String position;

    @NotBlank(message = "Số điện thoại của nhà tuyển dụng không được để trống")
    private String phone;

    @NotBlank(message = "Email của nhà tuyển dụng không được để trống")
    private String recruiterEmail;

    @NotBlank(message = "Website của công ty không được để trống")
    private String website;

    @NotBlank(message = "Mô tả công ty không được để trống")
    private String description;

    @NotBlank(message = "Địa chỉ công ty không được để trống")
    private String companyAddress;

    @NotBlank(message = "Logo công ty không được để trống")
    private String companyLogo;
}
