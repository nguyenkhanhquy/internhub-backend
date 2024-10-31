package com.internhub.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruiter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Recruiter {

    @Id
    private String userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "recruiter_name", nullable = false)
    private String recruiterName;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "recruiter_email", nullable = false)
    private String recruiterEmail;
}
