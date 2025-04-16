package com.internhub.backend.mapper;

import com.internhub.backend.dto.teacher.TeacherDTO;
import com.internhub.backend.entity.teacher.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "user.email", target = "email")
    TeacherDTO toDTO(Teacher teacher);

    List<TeacherDTO> toDTOs(List<Teacher> teachers);
}
