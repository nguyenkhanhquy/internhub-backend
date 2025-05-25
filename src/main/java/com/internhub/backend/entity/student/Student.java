package com.internhub.backend.entity.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.internhub.backend.entity.account.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Student {

    @Id
    private String userId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "exp_grad", nullable = false)
    private LocalDate expGrad;

    @Enumerated(EnumType.STRING)
    @Column(name = "major", nullable = false)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(name = "intern_status", nullable = false)
    private InternStatus internStatus;

    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name = "gpa", nullable = false)
    private double gpa;

    @Builder.Default
    @Column(name = "is_reported", nullable = false)
    private boolean isReported = false;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonBackReference
    private List<CurriculumVitae> curriculumVitaes = new ArrayList<>();
}
