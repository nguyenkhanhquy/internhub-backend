package com.internhub.backend.entity.student;

import com.internhub.backend.entity.account.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private boolean gender;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob", nullable = false)
    private Date dob;

    @Column(name = "exp_grad", nullable = false)
    private Date expGrad;

    @Enumerated(EnumType.STRING)
    @Column(name = "major", nullable = false)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(name = "intern_status", nullable = false)
    private InternStatus internStatus;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "gpa", nullable = false)
    private double gpa;
}
