package com.internhub.backend.entity.student;

import lombok.Getter;

@Getter
public enum InternStatus {

    SEARCHING("Đang tìm nơi thực tập"),
    WORKING("Đang thực tập"),
    COMPLETED("Đã thực tập"),
    ;

    private final String description;

    InternStatus(String description) {
        this.description = description;
    }
}
