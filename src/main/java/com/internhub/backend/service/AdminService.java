package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.student.InternshipReport;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface AdminService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    SuccessResponse<List<InternshipReport>> getAllInternshipReports(PageSearchSortFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void approveInternshipReport(String id);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void approveJobPost(String id);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void deleteJobPost(String id);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void approveRecruiter(String id);
}
