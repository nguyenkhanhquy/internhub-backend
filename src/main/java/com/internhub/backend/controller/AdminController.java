package com.internhub.backend.controller;

import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/internship-reports")
    public SuccessResponse<List<InternshipReport>> getAllInternshipReports(PageSearchSortFilterRequest request) {
        return adminService.getAllInternshipReports(request);
    }

    @GetMapping("/jobs")
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        return adminService.getAllJobPosts(request);
    }

    @PostMapping("/jobs/approve/{id}")
    public ResponseEntity<SuccessResponse<Void>> approveJobPost(@PathVariable("id") String id) {
        adminService.approveJobPost(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Duyệt bài đăng tuyển dụng thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/jobs/delete/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteJobPost(@PathVariable("id") String id) {
        adminService.deleteJobPost(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Từ chối bài đăng tuyển dụng thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/recruiters/approve/{id}")
    public ResponseEntity<SuccessResponse<Void>> approveRecruiter(@PathVariable("id") String id) {
        adminService.approveRecruiter(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Duyệt hồ sơ nhà tuyển dụng thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
