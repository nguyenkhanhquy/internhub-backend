package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobapply.JobApplyDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobApplyRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.job.ApplyStatus;
import com.internhub.backend.entity.job.JobApply;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobApplyMapper;
import com.internhub.backend.repository.JobApplyRepository;
import com.internhub.backend.repository.JobPostRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JobApplyServiceImpl implements JobApplyService {

    private final JobApplyRepository jobApplyRepository;
    private final StudentRepository studentRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplyMapper jobApplyMapper;

    @Override
    public SuccessResponse<List<JobApplyDetailDTO>> getJobApplyByStudent(JobPostSearchFilterRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        Sort sort = Sort.by(Sort.Order.desc("applyDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<JobApply> pageData = jobApplyRepository.findAllByStudent(student, request.getSearch(), pageable);

        return SuccessResponse.<List<JobApplyDetailDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobApplyMapper::toDetailDTO)
                        .toList())
                .build();
    }

    @Override
    public SuccessResponse<List<JobApplyDetailDTO>> getAllJobApplyByJobPostId(String jobPostId, PageSearchSortFilterRequest request) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        Sort sort = Sort.by(Sort.Order.desc("applyDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<JobApply> pageData = jobApplyRepository.findAllByJobPost(jobPost, pageable);

        return SuccessResponse.<List<JobApplyDetailDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobApplyMapper::toDetailDTO)
                        .toList())
                .build();
    }

    @Override
    public void createJobApply(CreateJobApplyRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        JobApply jobApply = JobApply.builder()
                .jobPost(jobPost)
                .student(student)
                .coverLetter(request.getCoverLetter())
                .cv(request.getCv())
                .applyStatus(ApplyStatus.PROCESSING)
                .applyDate(Date.from(Instant.now()))
                .build();

        jobApplyRepository.save(jobApply);
    }
}
