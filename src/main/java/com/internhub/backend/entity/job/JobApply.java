package com.internhub.backend.entity.job;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Lob
    @Column(name = "cover_letter", nullable = false)
    private String coverLetter;

    @Column(name = "cv", nullable = false)
    private String cv;

    @Column(name = "apply_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    @JsonManagedReference
    private JobPost jobPost;
}
