package com.internhub.backend.entity.student;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "internship_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InternshipReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(name = "instructor_email", nullable = false)
    private String instructorEmail;

    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "report_file", nullable = false)
    private String reportFile;

    @Column(name = "evaluation_file", nullable = false)
    private String evaluationFile;

    @Column(name = "is_system_company", nullable = false)
    private boolean isSystemCompany;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    private Student student;

    @Column(name = "report_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
}
