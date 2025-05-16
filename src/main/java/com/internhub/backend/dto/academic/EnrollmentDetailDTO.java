package com.internhub.backend.dto.academic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.internhub.backend.dto.student.StudentDTO;
import com.internhub.backend.entity.student.InternshipReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDetailDTO {

    private String id;

    private String courseCode;

    private String courseName;

    private String academicYear;

    private String semester;

    private StudentDTO student;

    private InternshipReport internshipReport;

    private String teacherName;

    private Double finalScore;

    private String feedback;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String enrollmentStatus;
}
