package com.internhub.backend.mapper;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.entity.academic.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "course.courseCode", target = "courseCode")
    @Mapping(source = "course.academicYear.name", target = "academicYear")
    @Mapping(source = "course.semester.description", target = "semester")
    @Mapping(source = "course.teacher.name", target = "teacherName")
    EnrollmentDTO toDTO(Enrollment enrollment);
}
