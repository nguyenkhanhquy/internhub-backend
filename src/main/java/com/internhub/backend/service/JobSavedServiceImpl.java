package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.job.JobSaved;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobPostMapper;
import com.internhub.backend.repository.JobPostRepository;
import com.internhub.backend.repository.JobSavedRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobSavedServiceImpl implements JobSavedService {

    private final JobSavedRepository jobSavedRepository;
    private final JobPostRepository jobPostRepository;
    private final StudentRepository studentRepository;
    private final JobPostMapper jobPostMapper;

    @Autowired
    public JobSavedServiceImpl(JobSavedRepository jobSavedRepository, JobPostRepository jobPostRepository, StudentRepository studentRepository, JobPostMapper jobPostMapper) {
        this.jobSavedRepository = jobSavedRepository;
        this.jobPostRepository = jobPostRepository;
        this.studentRepository = studentRepository;
        this.jobPostMapper = jobPostMapper;
    }

    @Override
    public SuccessResponse<List<JobPostBasicDTO>> getAllSavedJobPosts(JobPostSearchFilterRequest request) {
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

        List<JobPost> jobPosts;

        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            jobPosts = jobPostRepository.findByTitleContainingIgnoreCase(request.getSearch());
        } else {
            jobPosts = jobPostRepository.findAll();
        }


        List<JobPostBasicDTO> jobPostBasic = jobPosts.stream()
                .map(jobPostMapper::mapJobPostToJobPostBasicDTO)
                .filter(dto -> savedJobPostIds.contains(dto.getId())) // Chỉ giữ lại những DTO có jobPostId nằm trong savedJobPostIds
                .toList();

        int filteredTotalElements = jobPostBasic.size();
        int filteredTotalPages = (int) Math.ceil((double) filteredTotalElements / request.getSize());

        // Xác định kích thước trang và số trang từ request
        int pageNumber = request.getPage() - 1; // 0-based index
        int pageSize = request.getSize();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, jobPostBasic.size());

        // Kiểm tra điều kiện phân trang để tránh lỗi khi startIndex vượt quá kích thước của List
        List<JobPostBasicDTO> pageContent;
        if (startIndex < jobPostBasic.size()) {
            pageContent = jobPostBasic.subList(startIndex, endIndex);
        } else {
            pageContent = Collections.emptyList();
        }

        // Tạo đối tượng Page từ danh sách đã được phân trang
        Page<JobPostBasicDTO> pageData = new PageImpl<>(pageContent, PageRequest.of(pageNumber, pageSize), filteredTotalElements);

        return SuccessResponse.<List<JobPostBasicDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(filteredTotalPages)
                        .pageSize(request.getSize())
                        .totalElements(filteredTotalElements)
                        .hasPreviousPage(request.getPage() > 1)
                        .hasNextPage(request.getPage() < filteredTotalPages)
                        .build())
                .result(pageData.getContent())
                .build();
    }

    @Override
    public void deleteAllSavedJobPosts() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        jobSavedRepository.deleteAllByStudent(student);
    }
}
