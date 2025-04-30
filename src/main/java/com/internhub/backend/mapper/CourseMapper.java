package com.internhub.backend.mapper;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.entity.academic.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "academicYear.name", target = "academicYear")
    @Mapping(source = "semester.description", target = "semester")
    @Mapping(source = "teacher.teacherId", target = "teacherId")
    @Mapping(source = "teacher.name", target = "teacherName")
    @Mapping(source = "courseStatus.label", target = "courseStatus")
    CourseDTO toDTO(Course course);
}
