package com.internhub.backend.repository;

import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.job.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobPostRepository extends JpaRepository<JobPost, String> {

    @Query("SELECT j FROM JobPost j " +
            "WHERE j.company = :company AND " +
            "(:query IS NULL OR :query = '' OR " +
            "    LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.jobPosition) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ") " +
            "AND (:isApproved IS NULL OR j.isApproved = :isApproved) " +
            "AND (:isHidden IS NULL OR j.isHidden = :isHidden) " +
            "AND (:isDeleted IS NULL OR j.isDeleted = :isDeleted)")
    Page<JobPost> findAllByCompany(Company company, String query, Pageable pageable, Boolean isApproved, Boolean isHidden, Boolean isDeleted);

    @Query("SELECT j FROM JobPost j WHERE " +
            "(:query IS NULL OR :query = '' OR " +
            "    LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.type) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.remote) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.salary) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.company.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(j.jobPosition) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ") " +
            "AND j.isApproved = true AND j.isHidden = false AND j.isDeleted = false")
    Page<JobPost> searchJobPosts(String query, Pageable pageable);

    long countJobPostByIsApprovedAndIsDeleted(boolean isApproved, boolean isDeleted);
}
