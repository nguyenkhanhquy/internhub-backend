package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

public interface JobPostService {

    SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request);

    SuccessResponse<List<JobPostBasicDTO>> getPopularJobPosts(JobPostSearchFilterRequest request);

    JobPostDetailDTO getJobPostById(String id);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void createJobPost(CreateJobPostRequest createJobPostRequest);

    @PostAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void deleteJobPost(String id);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    boolean saveJobPost(Map<String, String> request);
}
