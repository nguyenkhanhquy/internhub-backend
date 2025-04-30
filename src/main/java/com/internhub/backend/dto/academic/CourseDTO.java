package com.internhub.backend.dto.academic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private String id;

    private String courseCode;

    private String courseName;

    private String academicYear;

    private String semester;

    private String teacherId;

    private String teacherName;

    private String courseStatus;

    private int totalStudents;

    private LocalDate startDate;

    private LocalDate endDate;
}
