package com.internhub.backend.controller;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.jobs.JobPostUpdateRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.JobPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobPostController {

    private final JobPostService jobPostService;

    @Autowired
    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobPostDetailDTO>>> getAllJobPosts(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getAllJobPosts(request));
    }

    @GetMapping("/popular")
    public ResponseEntity<SuccessResponse<List<JobPostBasicDTO>>> getPopularJobPosts(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getPopularJobPosts(request));
    }

    @GetMapping("/suitable")
    public ResponseEntity<SuccessResponse<List<JobPostBasicDTO>>> getJobPostsSuitableForStudent(@PageableDefault(page = 0, size = 10, sort = "updatedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<JobPostBasicDTO> pageData = jobPostService.getJobPostsSuitableForStudent(pageable);

        SuccessResponse<List<JobPostBasicDTO>> successResponse = SuccessResponse.<List<JobPostBasicDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(pageData.getNumber())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent())
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<JobPostDetailDTO>> getJobPostById(@PathVariable("id") String id) {
        JobPostDetailDTO jobPostDetailDTO = jobPostService.getJobPostById(id);

        SuccessResponse<JobPostDetailDTO> successResponse = SuccessResponse.<JobPostDetailDTO>builder()
                .result(jobPostDetailDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<SuccessResponse<List<JobPostDetailDTO>>> getAllJobPostByCompanyId(@PathVariable("id") String companyId, @ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getAllJobPostByCompanyId(companyId, request));
    }

    @GetMapping("/recruiter")
    public ResponseEntity<SuccessResponse<List<JobPostDetailDTO>>> getJobPostByRecruiter(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getJobPostByRecruiter(request));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createJobPost(@Valid @RequestBody CreateJobPostRequest createJobPostRequest) {
        jobPostService.createJobPost(createJobPostRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Tạo bài đăng thành công")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> updateJobPost(@PathVariable("id") String id, @Valid @RequestBody JobPostUpdateRequest request) {
        jobPostService.updateJobPost(id, request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật bài đăng thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable("id") String id) {
        jobPostService.deleteJobPost(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    public ResponseEntity<SuccessResponse<Void>> saveJobPost(@RequestBody Map<String, String> request) {
        String message = jobPostService.saveJobPost(request)
                ? "Lưu công việc thành công"
                : "Bỏ lưu công việc thành công";

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message(message)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/hidden/{id}")
    public ResponseEntity<SuccessResponse<Void>> hiddenJobPost(@PathVariable("id") String id) {
        String message = jobPostService.hiddenJobPost(id)
                ? "Ẩn bài đăng thành công"
                : "Hiện bài đăng thành công";

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message(message)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
