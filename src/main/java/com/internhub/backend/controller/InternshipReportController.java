package com.internhub.backend.controller;

import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.service.InternshipReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internship-reports")
@RequiredArgsConstructor
public class InternshipReportController {

    private final InternshipReportService internshipReportService;

    @GetMapping("/student")
    public SuccessResponse<List<InternshipReport>> getAllInternshipReportByStudent(PageSearchSortFilterRequest request) {
        return internshipReportService.getAllInternshipReportByStudent(request);
    }
}
