package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobSavedService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    SuccessResponse<List<JobPostBasicDTO>> getAllSavedJobPosts(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    @Transactional
    void deleteAllSavedJobPosts();
}
