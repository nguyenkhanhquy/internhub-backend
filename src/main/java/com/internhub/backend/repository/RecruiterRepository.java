package com.internhub.backend.repository;

import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.business.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepository extends JpaRepository<Recruiter, String> {

    Recruiter findByCompany(Company company);

    long countRecruiterByIsApproved(boolean isApproved);
}
