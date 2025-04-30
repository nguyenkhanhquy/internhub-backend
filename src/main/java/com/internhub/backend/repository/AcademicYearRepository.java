package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, String> {

    boolean existsByName(String name);

    AcademicYear findByName(String name);
}
