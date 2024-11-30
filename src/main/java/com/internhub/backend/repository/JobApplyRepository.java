package com.internhub.backend.repository;

import com.internhub.backend.entity.job.JobApply;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobApplyRepository extends JpaRepository<JobApply, String> {

    @Query("SELECT j FROM JobApply j " +
            "WHERE j.student = :student AND " +
            "(:query IS NULL OR " +
            "    LOWER(j.jobPost.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.jobPost.jobPosition) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.jobPost.company.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<JobApply> findAllByStudent(Student student, String query, Pageable pageable);
}
