package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobapply.JobApplyDetailDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.jobs.apply.CreateJobApplyRequest;
import com.internhub.backend.dto.request.jobs.apply.InterviewJobApplyRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface JobApplyService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void createJobApply(CreateJobApplyRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    SuccessResponse<List<JobApplyDetailDTO>> getJobApplyByStudent(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    SuccessResponse<List<JobApplyDetailDTO>> getAllJobApplyByJobPostId(String jobPostId, PageSearchSortFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void rejectJobApply(String jobApplyId);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void interviewJobApply(InterviewJobApplyRequest request);
}
