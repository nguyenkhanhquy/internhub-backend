package com.internhub.backend.mapper;

import com.internhub.backend.dto.student.MajorDTO;
import com.internhub.backend.entity.student.Major;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    MajorDTO mapMajorToMajorDTO(Major major);
}
