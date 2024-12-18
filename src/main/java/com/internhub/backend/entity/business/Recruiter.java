package com.internhub.backend.entity.business;

import com.internhub.backend.entity.account.User;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @MapsId
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "recruiter_email", nullable = false)
    private String recruiterEmail;

    @Builder.Default
    @Column(name = "is_approved", nullable = false)
    private boolean isApproved = false;
}
