package com.internhub.backend.controller;

import com.internhub.backend.dto.admin.OverviewDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.DeleteJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/overview")
    public ResponseEntity<SuccessResponse<OverviewDTO>> getOverview() {
        OverviewDTO overviewDTO = adminService.getOverview();

        SuccessResponse<OverviewDTO> successResponse = SuccessResponse.<OverviewDTO>builder()
                .result(overviewDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/internship-reports")
    public SuccessResponse<List<InternshipReport>> getAllInternshipReports(PageSearchSortFilterRequest request) {
        return adminService.getAllInternshipReports(request);
    }

    @PostMapping("/internship-reports/approve/{id}")
    public ResponseEntity<SuccessResponse<Void>> approveInternshipReport(@PathVariable("id") String id) {
        adminService.approveInternshipReport(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Duyệt báo cáo thực tập thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/internship-reports/reject/{id}")
    public ResponseEntity<SuccessResponse<Void>> rejectInternshipReport(@PathVariable("id") String id) {
        adminService.rejectInternshipReport(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Từ chối báo cáo thực tập thành công")
                .build();

        return ResponseEntity.ok(successResponse);
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
    public ResponseEntity<SuccessResponse<Void>> deleteJobPost(@PathVariable("id") String id, @Valid @RequestBody DeleteJobPostRequest request) {
        adminService.deleteJobPost(id, request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Từ chối bài đăng tuyển dụng thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/recruiters")
    public SuccessResponse<List<Recruiter>> getAllRecruiters(PageSearchSortFilterRequest request) {
        return adminService.getAllRecruiters(request);
    }

    @PostMapping("/recruiters/approve/{id}")
    public ResponseEntity<SuccessResponse<Void>> approveRecruiter(@PathVariable("id") String id) {
        adminService.approveRecruiter(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Duyệt hồ sơ nhà tuyển dụng thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/students")
    public SuccessResponse<List<Student>> getAllStudents(PageSearchSortFilterRequest request) {
        return adminService.getAllStudents(request);
    }

    @GetMapping("/teachers")
    public SuccessResponse<List<Teacher>> getAllTeachers(PageSearchSortFilterRequest request) {
        return adminService.getAllTeachers(request);
    }

    @GetMapping("/students/not-enrolled")
    public SuccessResponse<List<Student>> getAllStudentsNotEnrolledInSemester() {
        List<Student> students = adminService.getAllStudentsNotEnrolledInSemester();
        return SuccessResponse.<List<Student>>builder()
                .result(students)
                .build();
    }
}
