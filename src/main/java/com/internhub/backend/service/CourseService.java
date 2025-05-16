package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.courses.CreateCourseRequest;
import com.internhub.backend.dto.request.courses.UpdateCourseRequest;
import com.internhub.backend.dto.student.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CourseService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    CourseDTO createCourse(CreateCourseRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    Page<CourseDTO> getAllCourses(Pageable pageable, String search, String year, String semesterValue);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    CourseDTO getCourseById(String courseId);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    CourseDTO updateCourse(String courseId, UpdateCourseRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void deleteCourse(String courseId);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    List<StudentDTO> getAllStudentsByCourseId(String courseId);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void assignStudentsToCourse(String courseId, List<String> studentIds);
}
