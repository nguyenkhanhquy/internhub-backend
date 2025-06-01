package com.internhub.backend.controller;

import com.internhub.backend.dto.academic.OverviewDTO;
import com.internhub.backend.dto.academic.YearAndSemesterDTO;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.AcademicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/academic")
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicService academicService;

    @GetMapping("/year-and-semester")
    public ResponseEntity<SuccessResponse<YearAndSemesterDTO>> getAllYearAndSemester() {
        YearAndSemesterDTO yearAndSemesterDTO = academicService.getAllYearAndSemester();

        SuccessResponse<YearAndSemesterDTO> successResponse = SuccessResponse.<YearAndSemesterDTO>builder()
                .result(yearAndSemesterDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/overview")
    public ResponseEntity<SuccessResponse<OverviewDTO>> getOverview() {

        SuccessResponse<OverviewDTO> successResponse = SuccessResponse.<OverviewDTO>builder()
                .result(academicService.getOverview())
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
