package com.internhub.backend.entity.job;

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

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "salary", nullable = false)
    private String salary;

    @Column(name = "quantity", nullable = false)
    private String quantity;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date", nullable = false)
    private Date updatedDate;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "job_position", nullable = false)
    private String JobPosition;

    @Column(name = "requirements", nullable = false)
    private String requirements;

    @Column(name = "benefits", nullable = false)
    private String benefits;

    @ElementCollection(targetClass = Major.class)
    @CollectionTable(name = "job_post_majors", joinColumns = @JoinColumn(name = "job_post_id"))
    @Column(name = "major")
    @Enumerated(EnumType.STRING)
    private List<Major> majors;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(name = "address", nullable = false)
    private String address;

    private void addMajor(Major major) {
        majors.add(major);
    }

    private void removeMajor(Major major) {
        majors.remove(major);
    }
}
