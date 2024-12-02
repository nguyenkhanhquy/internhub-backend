package com.internhub.backend.controller;

import com.internhub.backend.dto.job.jobapply.JobApplyDetailDTO;
import com.internhub.backend.dto.request.jobs.CreateJobApplyRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.JobApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs/apply")
@RequiredArgsConstructor
public class JobApplyController {

    private final JobApplyService jobApplyService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createJobApply(@RequestBody CreateJobApplyRequest request) {
        jobApplyService.createJobApply(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Ứng tuyển thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/student")
    public ResponseEntity<SuccessResponse<List<JobApplyDetailDTO>>> getJobApplyByStudent(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobApplyService.getJobApplyByStudent(request));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<SuccessResponse<List<JobApplyDetailDTO>>> getAllJobApplyByJobPostId(@PathVariable("id") String jobPostId, @ModelAttribute PageSearchSortFilterRequest request) {
        return ResponseEntity.ok(jobApplyService.getAllJobApplyByJobPostId(jobPostId, request));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<SuccessResponse<Void>> rejectJobApply(@PathVariable("id") String jobApplyId) {
        jobApplyService.rejectJobApply(jobApplyId);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Từ chối hồ sơ thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
