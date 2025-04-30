package com.internhub.backend.dto.request.courses;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateCourseRequest {

    private String courseCode;

    private String courseName;

    private String academicYearId;

    private String semester;

    private String teacherId;

    private List<String> studentIds;
}
