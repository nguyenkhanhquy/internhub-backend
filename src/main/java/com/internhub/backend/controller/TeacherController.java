package com.internhub.backend.controller;

import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createTeacher(@Valid @RequestBody TeacherCreateRequest request) {
        teacherService.addTeacher(request);

        return ResponseEntity.ok(SuccessResponse.<Void>builder()
                .message("Thêm giáo viên thành công")
                .build());
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Teacher>>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();

        return ResponseEntity.ok(SuccessResponse.<List<Teacher>>builder()
                .result(teachers)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Teacher>> getTeacher(@PathVariable("id") String id) {
        Teacher teacher = teacherService.getTeacherById(id);

        return ResponseEntity.ok(SuccessResponse.<Teacher>builder()
                .result(teacher)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> updateTeacher(@PathVariable("id") String id, @Valid @RequestBody TeacherUpdateRequest request) {
        teacherService.updateTeacher(id, request);

        return ResponseEntity.ok(SuccessResponse.<Void>builder()
                .message("Cập nhật giáo viên thành công")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteTeacher(@PathVariable("id") String id) {
        teacherService.deleteTeacher(id);

        return ResponseEntity.ok(SuccessResponse.<Void>builder()
                .message("Xóa giáo viên thành công")
                .build());
    }

    @PostMapping("/import")
    public ResponseEntity<SuccessResponse<Void>> importTeachers(@RequestParam("file") MultipartFile file) {
        teacherService.importTeachersFromFile(file);

        return ResponseEntity.ok(SuccessResponse.<Void>builder()
                .message("Import giáo viên thành công")
                .build());
    }
}
