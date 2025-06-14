package com.internhub.backend.controller;

import com.internhub.backend.dto.job.jobapply.JobApplyDetailDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.jobs.apply.CreateJobApplyRequest;
import com.internhub.backend.dto.request.jobs.apply.InterviewJobApplyRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.JobApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/interview")
    public ResponseEntity<SuccessResponse<Void>> interviewJobApply(@RequestBody InterviewJobApplyRequest request) {
        jobApplyService.interviewJobApply(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Mời phỏng vấn thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/offer/{id}")
    public ResponseEntity<SuccessResponse<Void>> offerJobApply(@PathVariable("id") String jobApplyId) {
        jobApplyService.offerJobApply(jobApplyId);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Mời nhận việc thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/offer/accept/{id}")
    public ResponseEntity<SuccessResponse<Void>> acceptOfferJobApply(@PathVariable("id") String jobApplyId) {
        jobApplyService.acceptOfferJobApply(jobApplyId);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Chấp nhận thực tập thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/offer/refuse/{id}")
    public ResponseEntity<SuccessResponse<Void>> refuseOfferJobApply(@PathVariable("id") String jobApplyId) {
        jobApplyService.refuseOfferJobApply(jobApplyId);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Từ chối đề nghị thực tập thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/report-quit/{id}")
    public ResponseEntity<SuccessResponse<Void>> reportQuitJobApply(@PathVariable("id") String jobApplyId, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");

        jobApplyService.reportQuitJobApply(jobApplyId, reason);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Báo cáo bỏ việc thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
