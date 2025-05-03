package com.internhub.backend.controller;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student")
    public ResponseEntity<SuccessResponse<List<EnrollmentDTO>>> getAllEnrollmentsByStudent(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                                           @RequestParam(required = false) String search) {
        Page<EnrollmentDTO> pageData = enrollmentService.getAllEnrollmentsByStudent(pageable, search);

        SuccessResponse<List<EnrollmentDTO>> successResponse = SuccessResponse.<List<EnrollmentDTO>>builder()
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
}
