package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.courses.CreateCourseRequest;
import com.internhub.backend.dto.request.courses.UpdateCourseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CourseService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    CourseDTO createCourse(CreateCourseRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    Page<CourseDTO> getAllCourses(Pageable pageable, String search, String year, String semester);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    CourseDTO getCourseById(String courseId);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    CourseDTO updateCourse(String courseId, UpdateCourseRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void deleteCourse(String courseId);
}
