package com.internhub.backend.service;

import com.internhub.backend.dto.cv.CurriculumVitaeDTO;
import com.internhub.backend.dto.request.cv.CreateCVRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CurriculumVitaeService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void createCurriculumVitae(CreateCVRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    Page<CurriculumVitaeDTO> getAllCVsByStudent(Pageable pageable, String search);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void deleteCurriculumVitae(String cvId);
}
