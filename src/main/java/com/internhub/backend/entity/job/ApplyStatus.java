package com.internhub.backend.entity.job;

import lombok.Getter;

@Getter
public enum ApplyStatus {

    APPLIED("Đã ứng tuyển"),
    PENDING("Chờ xử lý"),
    ACCEPTED("Chấp nhận"),
    REJECTED("Từ chối"),
    ;

    private final String description;

    ApplyStatus(String description) {
        this.description = description;
    }
}
