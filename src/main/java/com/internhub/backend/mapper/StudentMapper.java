package com.internhub.backend.mapper;

import com.internhub.backend.dto.student.StudentDTO;
import com.internhub.backend.entity.student.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO mapStudentToStudentDTO(Student student);
}
