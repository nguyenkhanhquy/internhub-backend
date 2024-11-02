package com.internhub.backend.repository;

import com.internhub.backend.entity.business.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {

    boolean existsByName(String name);
}
