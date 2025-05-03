package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.jobs.JobPostUpdateRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

public interface JobPostService {

    SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request);

    SuccessResponse<List<JobPostBasicDTO>> getPopularJobPosts(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    Page<JobPostBasicDTO> getJobPostsSuitableForStudent(Pageable pageable);

    JobPostDetailDTO getJobPostById(String id);

    SuccessResponse<List<JobPostDetailDTO>> getAllJobPostByCompanyId(String companyId, JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    SuccessResponse<List<JobPostDetailDTO>> getJobPostByRecruiter(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void createJobPost(CreateJobPostRequest createJobPostRequest);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void updateJobPost(String id, JobPostUpdateRequest request);

    @PostAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void deleteJobPost(String id);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    boolean saveJobPost(Map<String, String> request);

    @PostAuthorize("hasAuthority('SCOPE_RECRUITER')")
    boolean hiddenJobPost(String id);
}
