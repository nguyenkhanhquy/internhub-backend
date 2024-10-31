package com.internhub.backend.entity;

import lombok.Getter;

@Getter
public enum Major {

    IT("Công nghệ thông tin"),
    DS("Kỹ thuật dữ liệu"),
    IS("An toàn thông tin"),
    ;

    private final String description;

    Major(String description) {
        this.description = description;
    }
}
