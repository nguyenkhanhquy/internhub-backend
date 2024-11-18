package com.internhub.backend.service;

import com.internhub.backend.dto.job.jobsaved.JobSavedDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.job.JobSaved;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobSavedMapper;
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

@Service
public class JobSavedServiceImpl implements JobSavedService {

    private final JobSavedRepository jobSavedRepository;
    private final StudentRepository studentRepository;
    private final JobSavedMapper jobSavedMapper;

    @Autowired
    public JobSavedServiceImpl(JobSavedRepository jobSavedRepository, StudentRepository studentRepository, JobSavedMapper jobSavedMapper) {
        this.jobSavedRepository = jobSavedRepository;
        this.studentRepository = studentRepository;
        this.jobSavedMapper = jobSavedMapper;
    }

    @Override
    public SuccessResponse<List<JobSavedDTO>> getAllSavedJobPosts(JobPostSearchFilterRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        List<JobSaved> jobSavedList = jobSavedRepository.findByStudent(student);

        List<JobSavedDTO> jobSavedDTOs = jobSavedList.stream()
                .map(jobSavedMapper::mapJobSavedToJobSavedDTO)
                .filter(job -> {
                    String search = request.getSearch();
                    if (search == null || search.trim().isEmpty()) return true;

                    String searchLower = search.toLowerCase().trim();
                    return (job.getTitle() != null && job.getTitle().toLowerCase().contains(searchLower)) ||
                            (job.getJobPosition() != null && job.getJobPosition().toLowerCase().contains(searchLower)) ||
                            (job.getCompanyName() != null && job.getCompanyName().toLowerCase().contains(searchLower));
                })
                .sorted((a, b) -> b.getSavedDate().compareTo(a.getSavedDate())) // Sắp xếp theo thời gian lưu mới nhất (Giảm dần)
//                .sorted((a, b) -> a.getSavedDate().compareTo(b.getSavedDate())) // Sắp xếp theo thời gian lưu cũ nhất (Tăng dần)
                .toList();

        int filteredTotalElements = jobSavedDTOs.size();
        int filteredTotalPages = (int) Math.ceil((double) filteredTotalElements / request.getSize());

        // Xác định kích thước trang và số trang từ request
        int pageNumber = request.getPage() - 1; // 0-based index
        int pageSize = request.getSize();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, jobSavedDTOs.size());

        // Kiểm tra điều kiện phân trang để tránh lỗi khi startIndex vượt quá kích thước của List
        List<JobSavedDTO> pageContent;
        if (startIndex < jobSavedDTOs.size()) {
            pageContent = jobSavedDTOs.subList(startIndex, endIndex);
        } else {
            pageContent = Collections.emptyList();
        }

        Page<JobSavedDTO> pageData = new PageImpl<>(
                pageContent,
                PageRequest.of(pageNumber, pageSize),
                filteredTotalElements
        );

        return SuccessResponse.<List<JobSavedDTO>>builder()
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
