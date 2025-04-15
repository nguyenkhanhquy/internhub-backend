package com.internhub.backend.entity.academic;

import lombok.Getter;

@Getter
public enum Semester {

    HK01("HK01", "Học kỳ 1"),
    HK02("HK02", "Học kỳ 2"),
    HK03("HK03", "Học kỳ 3"),
    ;

    private final String value;
    private final String description;

    Semester(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
