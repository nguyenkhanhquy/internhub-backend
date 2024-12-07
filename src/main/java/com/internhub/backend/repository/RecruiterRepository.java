package com.internhub.backend.repository;

import com.internhub.backend.entity.business.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecruiterRepository extends JpaRepository<Recruiter, String> {

    @Query("SELECT r FROM Recruiter r WHERE" +
            "(:query IS NULL OR :query = '' OR " +
            "    LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(r.company.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ")")
    Page<Recruiter> findAllRecruiters(String query, Pageable pageable);

    long countRecruiterByIsApproved(boolean isApproved);
}
