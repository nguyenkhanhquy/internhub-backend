package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobsaved.JobSavedDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface JobSavedService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    SuccessResponse<List<JobSavedDTO>> getAllSavedJobPosts(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void deleteAllSavedJobPosts();
}
