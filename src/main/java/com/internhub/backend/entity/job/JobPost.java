package com.internhub.backend.entity.job;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.student.Major;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "job_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "remote", nullable = false)
    private String remote;

    @Column(name = "description", length = 3000, nullable = false)
    private String description;

    @Column(name = "salary", nullable = false)
    private String salary;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "created_date", updatable = false, nullable = false)
    private Date createdDate;

    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "job_position", nullable = false)
    private String jobPosition;

    @Column(name = "requirements", length = 3000, nullable = false)
    private String requirements;

    @Column(name = "benefits", length = 3000, nullable = false)
    private String benefits;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonBackReference
    private List<JobApply> jobApplies;

    @ElementCollection(targetClass = Major.class)
    @CollectionTable(name = "job_post_majors", joinColumns = @JoinColumn(name = "job_post_id"))
    @Column(name = "major", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Major> majors;

    private void addMajor(Major major) {
        majors.add(major);
    }

    private void removeMajor(Major major) {
        majors.remove(major);
    }
}
