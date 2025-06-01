package com.internhub.backend.controller;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.dto.request.students.UpdateStudentProfileRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();

        SuccessResponse<List<Student>> successResponse = SuccessResponse.<List<Student>>builder()
                .result(students)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<SuccessResponse<Void>> updateStudentProfile(@Valid @RequestBody UpdateStudentProfileRequest request) {
        studentService.updateStudentProfile(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật hồ sơ thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/current-enrollment")
    public ResponseEntity<SuccessResponse<EnrollmentDTO>> getCurrentEnrollmentByStudent() {
        EnrollmentDTO enrollmentDTO = studentService.getCurrentEnrollmentByStudent();

        SuccessResponse<EnrollmentDTO> successResponse = SuccessResponse.<EnrollmentDTO>builder()
                .result(enrollmentDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
