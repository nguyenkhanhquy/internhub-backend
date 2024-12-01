package com.internhub.backend.entity.job;

import lombok.Getter;

@Getter
public enum ApplyStatus {

    PROCESSING("Đang xử lý"),
    INTERVIEW("Phỏng vấn"),
    OFFER("Đề nghị"),
    REJECTED("Đã từ chối"),
    ACCEPTED("Đã nhận"),
    REFUSED("Không nhận")
    ;

    private final String description;

    ApplyStatus(String description) {
        this.description = description;
    }
}
