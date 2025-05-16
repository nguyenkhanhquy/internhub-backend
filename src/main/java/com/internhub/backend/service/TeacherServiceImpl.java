package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.dto.teacher.TeacherDTO;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.CourseMapper;
import com.internhub.backend.mapper.TeacherMapper;
import com.internhub.backend.repository.CourseRepository;
import com.internhub.backend.repository.TeacherRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
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
}
