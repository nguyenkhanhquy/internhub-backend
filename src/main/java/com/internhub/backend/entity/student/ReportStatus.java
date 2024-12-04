package com.internhub.backend.entity.student;

import lombok.Getter;

@Getter
public enum ReportStatus {

    PROCESSING("Đang xử lý"),
    REJECTED("Không được duyệt"),
    ACCEPTED("Đã duyệt");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }
}
