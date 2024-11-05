package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobPostMapper;
import com.internhub.backend.repository.JobPostRepository;
import com.internhub.backend.repository.RecruiterRepository;
import com.internhub.backend.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobPostMapper jobPostMapper;

    @Autowired
    public JobPostServiceImpl(JobPostRepository jobPostRepository, RecruiterRepository recruiterRepository, JobPostMapper jobPostMapper) {
        this.jobPostRepository = jobPostRepository;
        this.recruiterRepository = recruiterRepository;
        this.jobPostMapper = jobPostMapper;
    }

    @Override
    public SuccessResponse<List<JobPostBasicDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            pageData = jobPostRepository.findByTitleContainingIgnoreCase(request.getSearch(), pageable);
        } else {
            pageData = jobPostRepository.findAll(pageable);
        }

        return SuccessResponse.<List<JobPostBasicDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobPostMapper::mapJobPostToJobPostBasicDTO)
                        .toList())
                .build();
    }

    @Override
    public JobPostDetailDTO getJobPostById(String id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        return jobPostMapper.mapJobPostToJobPostDetailDTO(jobPost);
    }

    @Override
    public void createJobPost(CreateJobPostRequest createJobPostRequest) {
        Authentication authentication = SecurityUtil.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Recruiter recruiter = recruiterRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        JobPost jobPost = JobPost.builder()
                .title(createJobPostRequest.getTitle())
                .type(createJobPostRequest.getType())
                .remote(createJobPostRequest.getRemote())
                .description(createJobPostRequest.getDescription())
                .salary(createJobPostRequest.getSalary())
                .quantity(createJobPostRequest.getQuantity())
                .createdDate(Date.from(Instant.now()))
                .updatedDate(Date.from(Instant.now()))
                .expiryDate(createJobPostRequest.getExpiryDate())
                .jobPosition(createJobPostRequest.getJobPosition())
                .requirements(createJobPostRequest.getRequirements())
                .benefits(createJobPostRequest.getBenefits())
                .company(recruiter.getCompany())
                .address(createJobPostRequest.getAddress())
                .majors(createJobPostRequest.getMajors())
                .build();

        jobPostRepository.save(jobPost);
    }

    @Override
    public void deleteJobPost(String id) {
        jobPostRepository.deleteById(id);
    }
}
