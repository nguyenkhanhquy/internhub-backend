package com.internhub.backend.controller;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.JobPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<JobPostDetailDTO>> getJobPostById(@PathVariable("id") String id) {
        JobPostDetailDTO jobPostDetailDTO = jobPostService.getJobPostById(id);

        SuccessResponse<JobPostDetailDTO> successResponse = SuccessResponse.<JobPostDetailDTO>builder()
                .result(jobPostDetailDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createJobPost(@Valid @RequestBody CreateJobPostRequest createJobPostRequest) {
        jobPostService.createJobPost(createJobPostRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Tạo bài đăng việc làm thành công")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable("id") String id) {
        jobPostService.deleteJobPost(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    public ResponseEntity<SuccessResponse<Void>> saveJobPost(@RequestBody Map<String, String> request) {
        String message = jobPostService.saveJobPost(request)
                ? "Công việc làm đã được lưu"
                : "Đã bỏ lưu công việc";

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message(message)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
