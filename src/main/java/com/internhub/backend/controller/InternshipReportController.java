package com.internhub.backend.controller;

import com.internhub.backend.dto.request.internshipReports.CreateInternshipReportRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.service.InternshipReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internship-reports")
@RequiredArgsConstructor
public class InternshipReportController {

    private final InternshipReportService internshipReportService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createInternshipReport(@Valid @RequestBody CreateInternshipReportRequest request) {
        internshipReportService.createInternshipReport(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Nộp báo cáo thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/student")
    public SuccessResponse<List<InternshipReport>> getAllInternshipReportsByStudent(PageSearchSortFilterRequest request) {
        return internshipReportService.getAllInternshipReportsByStudent(request);
    }
}
