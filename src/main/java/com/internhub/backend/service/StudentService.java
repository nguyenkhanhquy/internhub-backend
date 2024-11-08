package com.internhub.backend.service;

import com.internhub.backend.dto.request.students.UpdateStudentProfileRequest;

public interface StudentService {

    void updateStudentProfile(UpdateStudentProfileRequest request);
}
