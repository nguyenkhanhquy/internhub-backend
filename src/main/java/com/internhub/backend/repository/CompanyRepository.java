package com.internhub.backend.repository;

import com.internhub.backend.entity.business.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, String> {

    boolean existsByName(String name);

    @Query("SELECT c FROM Company c WHERE c.recruiter.isApproved = true")
    Page<Company> findAllApprovedCompanies(Pageable pageable);
}
