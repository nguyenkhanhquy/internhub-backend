package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobapply.JobApplyDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobApplyRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface JobApplyService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void createJobApply(CreateJobApplyRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    SuccessResponse<List<JobApplyDetailDTO>> getJobApplyByStudent(JobPostSearchFilterRequest request);
}
