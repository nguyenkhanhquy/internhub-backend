package com.internhub.backend.service;

import com.internhub.backend.dto.request.jobs.CreateJobApplyRequest;
import org.springframework.security.access.prepost.PreAuthorize;

public interface JobApplyService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void createJobApply(CreateJobApplyRequest request);
}
