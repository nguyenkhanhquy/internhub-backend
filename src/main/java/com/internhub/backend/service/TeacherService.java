package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.dto.teacher.TeacherDTO;
import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface TeacherService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void addTeacher(TeacherCreateRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void updateTeacher(String id, TeacherUpdateRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void deleteTeacher(String id);

    Teacher getTeacherById(String id);

    List<TeacherDTO> getAllTeachers();

    @PreAuthorize("hasAuthority('SCOPE_TEACHER')")
    Page<CourseDTO> getAllCoursesByTeacher(Pageable pageable, String search, String year, String semesterValue);
}
