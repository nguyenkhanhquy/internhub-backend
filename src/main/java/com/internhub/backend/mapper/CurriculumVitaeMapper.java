package com.internhub.backend.mapper;

import com.internhub.backend.dto.cv.CurriculumVitaeDTO;
import com.internhub.backend.entity.student.CurriculumVitae;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurriculumVitaeMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    CurriculumVitaeDTO toDTO(CurriculumVitae curriculumVitae);
}
