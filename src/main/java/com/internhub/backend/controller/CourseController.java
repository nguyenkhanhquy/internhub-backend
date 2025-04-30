package com.internhub.backend.controller;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.courses.CreateCourseRequest;
import com.internhub.backend.dto.request.courses.UpdateCourseRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CourseDTO>> createCourse(@RequestBody CreateCourseRequest request) {
        CourseDTO courseDTO = courseService.createCourse(request);

        SuccessResponse<CourseDTO> response = SuccessResponse.<CourseDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Đã tạo lớp học")
                .result(courseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CourseDTO>>> getAllCourses(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                          @RequestParam(required = false) String search,
                                                                          @RequestParam(required = false) String year,
                                                                          @RequestParam(required = false) String semester) {
        Page<CourseDTO> pageData = courseService.getAllCourses(pageable, search, year, semester);

        SuccessResponse<List<CourseDTO>> response = SuccessResponse.<List<CourseDTO>>builder()
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

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<SuccessResponse<CourseDTO>> getCourseById(@PathVariable String courseId) {
        CourseDTO course = courseService.getCourseById(courseId);

        SuccessResponse<CourseDTO> response = SuccessResponse.<CourseDTO>builder()
                .result(course)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<SuccessResponse<CourseDTO>> updateCourse(@PathVariable String courseId, @RequestBody UpdateCourseRequest request) {
        CourseDTO courseDTO = courseService.updateCourse(courseId, request);

        SuccessResponse<CourseDTO> response = SuccessResponse.<CourseDTO>builder()
                .message("Đã cập nhật lớp học")
                .result(courseDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);

        return ResponseEntity.noContent().build();
    }
}
