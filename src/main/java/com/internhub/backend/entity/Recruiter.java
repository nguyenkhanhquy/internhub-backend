package com.internhub.backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "company")
    private String company;

    @Column(name = "recruiter_name")
    private String recruiterName;

    @Column(name = "position")
    private String position;

    @Column(name = "phone")
    private String phone;

    @Column(name = "recruiter_email")
    private String recruiterEmail;
}
