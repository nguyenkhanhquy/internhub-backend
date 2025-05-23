package com.internhub.backend.service;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface EnrollmentService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    Page<EnrollmentDTO> getAllEnrollmentsByStudent(Pageable pageable, String search);

    @PreAuthorize("hasAuthority('SCOPE_TEACHER')")
    void updateFinalScore(String enrollmentId, Double finalScore, String feedback);
}
