package com.internhub.backend.controller;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.dto.request.enrollments.UpdateFinalScoreRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student")
    public ResponseEntity<SuccessResponse<List<EnrollmentDTO>>> getAllEnrollmentsByStudent(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
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

    @PatchMapping("/{enrollmentId}/final-score")
    public ResponseEntity<SuccessResponse<Void>> updateFinalScore(@PathVariable String enrollmentId,
                                                                  @Valid @RequestBody UpdateFinalScoreRequest request) {
        enrollmentService.updateFinalScore(enrollmentId, request.getFinalScore(), request.getFeedback());

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật điểm thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
