package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.job.JobSaved;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobPostMapper;
import com.internhub.backend.repository.JobPostRepository;
import com.internhub.backend.repository.JobSavedRepository;
import com.internhub.backend.repository.RecruiterRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.AuthUtils;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;
    private final JobSavedRepository jobSavedRepository;
    private final JobPostMapper jobPostMapper;

    @Autowired
    public JobPostServiceImpl(JobPostRepository jobPostRepository, RecruiterRepository recruiterRepository, StudentRepository studentRepository, JobSavedRepository jobSavedRepository, JobPostMapper jobPostMapper) {
        this.jobPostRepository = jobPostRepository;
        this.recruiterRepository = recruiterRepository;
        this.studentRepository = studentRepository;
        this.jobSavedRepository = jobSavedRepository;
        this.jobPostMapper = jobPostMapper;
    }

    @Override
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("createdDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("createdDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData = jobPostRepository.searchJobPosts(request.getSearch(), pageable);

        try {
            Authentication authentication = AuthUtils.getAuthenticatedUser();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String userId = (String) jwt.getClaims().get("userId");

            Student student = studentRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

            List<JobSaved> jobSavedList = jobSavedRepository.findByStudent(student);

            // Tạo danh sách ID của các công việc đã lưu
            Set<String> savedJobPostIds = jobSavedList.stream()
                    .map(jobSaved -> jobSaved.getJobPost().getId())
                    .collect(Collectors.toSet());

            List<JobPostDetailDTO> jobPostDetails = pageData.getContent().stream()
                    .map(jobPost -> {
                        JobPostDetailDTO dto = jobPostMapper.mapJobPostToJobPostDetailDTO(jobPost);
                        // Kiểm tra nếu jobPostId nằm trong savedJobPostIds, cập nhật isSaved
                        if (savedJobPostIds.contains(jobPost.getId())) {
                            dto.setSaved(true);
                        }
                        return dto;
                    })
                    .toList();

            return SuccessResponse.<List<JobPostDetailDTO>>builder()
                    .pageInfo(SuccessResponse.PageInfo.builder()
                            .currentPage(request.getPage())
                            .totalPages(pageData.getTotalPages())
                            .pageSize(pageData.getSize())
                            .totalElements(pageData.getTotalElements())
                            .hasPreviousPage(pageData.hasPrevious())
                            .hasNextPage(pageData.hasNext())
                            .build())
                    .result(jobPostDetails)
                    .build();
        } catch (CustomException e) {
            return SuccessResponse.<List<JobPostDetailDTO>>builder()
                    .pageInfo(SuccessResponse.PageInfo.builder()
                            .currentPage(request.getPage())
                            .totalPages(pageData.getTotalPages())
                            .pageSize(pageData.getSize())
                            .totalElements(pageData.getTotalElements())
                            .hasPreviousPage(pageData.hasPrevious())
                            .hasNextPage(pageData.hasNext())
                            .build())
                    .result(pageData.getContent().stream()
                            .map(jobPostMapper::mapJobPostToJobPostDetailDTO)
                            .toList())
                    .build();
        }
    }

    @Override
    public SuccessResponse<List<JobPostBasicDTO>> getPopularJobPosts(JobPostSearchFilterRequest request) {
        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("createdDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("createdDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData = jobPostRepository.searchJobPosts(request.getSearch(), pageable);

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

        JobPostDetailDTO jobPostDetailDTO = jobPostMapper.mapJobPostToJobPostDetailDTO(jobPost);

        try {
            Authentication authentication = AuthUtils.getAuthenticatedUser();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String userId = (String) jwt.getClaims().get("userId");

            Student student = studentRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

            if (jobSavedRepository.existsByStudentAndJobPost(student, jobPost)) {
                jobPostDetailDTO.setSaved(true);
            }
            return jobPostDetailDTO;
        } catch (CustomException e) {
            return jobPostDetailDTO;
        }
    }

    @Override
    public void createJobPost(CreateJobPostRequest createJobPostRequest) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
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

    @Override
    public boolean saveJobPost(Map<String, String> request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        JobPost jobPost = jobPostRepository.findById(request.get("id"))
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        JobSaved jobSaved = jobSavedRepository.findByStudentAndJobPost(student, jobPost);
        if (jobSaved != null) {
            jobSavedRepository.delete(jobSaved);
            return false;
        }

        jobSavedRepository.save(JobSaved.builder()
                .savedDate(Date.from(Instant.now()))
                .student(student)
                .jobPost(jobPost)
                .build());

        return true;
    }
}
