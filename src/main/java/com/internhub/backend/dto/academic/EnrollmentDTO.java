package com.internhub.backend.dto.academic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {

    private String id;

    private String courseCode;

    private String academicYear;

    private String semester;

    private String teacherName;

    private Double finalScore;

    private String enrollmentStatus;
}
