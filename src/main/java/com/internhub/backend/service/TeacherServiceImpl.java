package com.internhub.backend.service;

import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.dto.teacher.TeacherDTO;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.TeacherMapper;
import com.internhub.backend.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

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
}
