package com.internhub.backend.repository;

import com.internhub.backend.entity.student.CurriculumVitae;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumVitaeRepository extends JpaRepository<CurriculumVitae, String> {

    Page<CurriculumVitae> findByStudent_UserId(Pageable pageable, String userId);

    Page<CurriculumVitae> findByStudent_UserIdAndTitleContainingIgnoreCase(Pageable pageable, String userId, String title);
}
