package com.internhub.backend.service;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.dto.request.students.UpdateStudentProfileRequest;
import com.internhub.backend.entity.student.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    List<Student> getAllStudents();

    void updateStudentProfile(UpdateStudentProfileRequest request);

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    EnrollmentDTO getCurrentEnrollmentByStudent();

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void importStudents(MultipartFile file);
}
