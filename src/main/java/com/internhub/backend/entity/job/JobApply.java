package com.internhub.backend.entity.job;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.internhub.backend.entity.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "job_apply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobApply {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "apply_date", updatable = false, nullable = false)
    private Date applyDate;

    @Column(name = "cover_letter", length = 3000, nullable = false)
    private String coverLetter;

    @Column(name = "cv", nullable = false)
    private String cv;

    @Column(name = "apply_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    @JsonManagedReference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "job_post_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    @JsonManagedReference
    private JobPost jobPost;
}
