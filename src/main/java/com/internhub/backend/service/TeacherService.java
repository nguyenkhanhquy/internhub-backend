package com.internhub.backend.service;

import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface TeacherService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    public void addTeacher(TeacherCreateRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    public void updateTeacher(String id, TeacherUpdateRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    public void deleteTeacher(String id);

    public Teacher getTeacherById(String id);

    public List<Teacher> getAllTeachers();
}
