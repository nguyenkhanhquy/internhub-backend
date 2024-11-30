package com.internhub.backend.service;

import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void addTeacher(TeacherCreateRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void updateTeacher(String id, TeacherUpdateRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void deleteTeacher(String id);

    Teacher getTeacherById(String id);

    List<Teacher> getAllTeachers();

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void importTeachersFromFile(MultipartFile file);
}
