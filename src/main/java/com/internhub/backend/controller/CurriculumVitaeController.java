package com.internhub.backend.controller;

import com.internhub.backend.dto.cv.CurriculumVitaeDTO;
import com.internhub.backend.dto.request.cv.CreateCVRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.CurriculumVitaeService;
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
@RequestMapping("/curriculum-vitae")
@RequiredArgsConstructor
public class CurriculumVitaeController {

    private final CurriculumVitaeService curriculumVitaeService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createCurriculumVitae(@Valid @RequestBody CreateCVRequest request) {
        curriculumVitaeService.createCurriculumVitae(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Tạo CV thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/student")
    public ResponseEntity<SuccessResponse<List<CurriculumVitaeDTO>>> getAllCVsByStudent(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                                                        @RequestParam (required = false) String search) {
        Page<CurriculumVitaeDTO> pageData = curriculumVitaeService.getAllCVsByStudent(pageable, search);

        SuccessResponse<List<CurriculumVitaeDTO>> successResponse = SuccessResponse.<List<CurriculumVitaeDTO>>builder()
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

    @DeleteMapping("/{cvId}")
    public ResponseEntity<SuccessResponse<Void>> deleteCurriculumVitae(@PathVariable String cvId) {
        curriculumVitaeService.deleteCurriculumVitae(cvId);

        return ResponseEntity.noContent().build();
    }
}
