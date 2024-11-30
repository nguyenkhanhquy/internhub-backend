package com.internhub.backend.entity.job;

import com.internhub.backend.entity.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "job_saved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobSaved {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "saved_date", updatable = false, nullable = false)
    private Date savedDate;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @ManyToOne
    @JoinColumn(name = "job_post_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private JobPost jobPost;
}
