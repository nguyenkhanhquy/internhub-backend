package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.dto.teacher.TeacherDTO;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.CourseMapper;
import com.internhub.backend.mapper.TeacherMapper;
import com.internhub.backend.repository.CourseRepository;
import com.internhub.backend.repository.RoleRepository;
import com.internhub.backend.repository.TeacherRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.util.AuthUtils;
import com.internhub.backend.util.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    @Value("${admin.password}")
    private String adminPassword;

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherMapper teacherMapper;
    private final CourseMapper courseMapper;

    @Override
    public void addTeacher(TeacherCreateRequest request) {
        Teacher teacher = Teacher.builder()
                .name(request.getName())
                .teacherId(request.getTeacherId())
                .build();

        teacherRepository.save(teacher);
    }

    @Override
    public void updateTeacher(String id, TeacherUpdateRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.TEACHER_NOT_FOUND));

        teacher.setName(request.getName());
        teacher.setTeacherId(request.getTeacherId());

        teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(String id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.TEACHER_NOT_FOUND));

        teacherRepository.delete(teacher);
    }

    @Override
    public Teacher getTeacherById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.TEACHER_NOT_FOUND));
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherMapper.toDTOs(teacherRepository.findAll());
    }

    @Override
    public Page<CourseDTO> getAllCoursesByTeacher(Pageable pageable, String search, String year, String semesterValue) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Teacher teacher = teacherRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        Semester semester = Optional.ofNullable(semesterValue)
                .filter(s -> !s.isBlank())
                .map(s -> {
                    try {
                        return Semester.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .orElse(null);

        return courseRepository.filterCoursesByTeacher(pageable, teacher, search, year, semester)
                .map(courseMapper::toDTO);
    }

    @Override
    public void importTeachers(MultipartFile file) {
        if (!ExcelUtils.isExcelFile(file)) {
            throw new CustomException(EnumException.FILE_TYPE_INVALID);
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Teacher> teachers = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua tiêu đề

                String teacherId = ExcelUtils.getCellValueAsString(row.getCell(0));
                String name = ExcelUtils.getCellValueAsString(row.getCell(1));
                String email = ExcelUtils.getCellValueAsString(row.getCell(2));

                if (email == null || email.isEmpty()) continue;

                if (userRepository.existsByEmail(email)) {
                    log.warn("Email {} đã tồn tại, bỏ qua.", email);
                    continue;
                }

                User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(adminPassword))
                        .isActive(false)
                        .role(roleRepository.findByName("TEACHER"))
                        .build();

                User savedUser = userRepository.save(user);

                Teacher teacher = Teacher.builder()
                        .user(savedUser)
                        .name(name)
                        .teacherId(teacherId)
                        .build();

                teachers.add(teacher);
            }

            teacherRepository.saveAll(teachers);
        } catch (Exception e) {
            throw new CustomException(EnumException.IMPORT_FILE_ERROR);
        }
    }
}
