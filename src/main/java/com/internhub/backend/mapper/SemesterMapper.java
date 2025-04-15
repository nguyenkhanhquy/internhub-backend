package com.internhub.backend.mapper;

import com.internhub.backend.dto.academic.SemesterDTO;
import com.internhub.backend.entity.academic.Semester;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SemesterMapper {

    @Mapping(source = "value", target = "id")
    @Mapping(source = "description", target = "name")
    SemesterDTO toDTO(Semester semester);
}
