package com.internhub.backend.entity.student;

import lombok.Getter;

@Getter
public enum Major {

    IT("IT", "Công nghệ thông tin"),
    DS("DS", "Kỹ thuật dữ liệu"),
    IS("IS", "An toàn thông tin"),
    ;

    private final String value;
    private final String description;

    Major(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
