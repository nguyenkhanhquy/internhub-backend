package com.internhub.backend.controller;

import com.internhub.backend.dto.request.students.UpdateStudentProfileRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/update-profile")
    public ResponseEntity<SuccessResponse<Void>> updateProfile(@Valid @RequestBody UpdateStudentProfileRequest request) {
        studentService.updateStudentProfile(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật hồ sơ thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
