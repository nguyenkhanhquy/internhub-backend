package com.internhub.backend.service;

import com.internhub.backend.dto.request.internshipReports.CreateInternshipReportRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.student.InternshipReport;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface InternshipReportService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    void createInternshipReport(CreateInternshipReportRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    SuccessResponse<List<InternshipReport>> getAllInternshipReportsByStudent(PageSearchSortFilterRequest request);
}
