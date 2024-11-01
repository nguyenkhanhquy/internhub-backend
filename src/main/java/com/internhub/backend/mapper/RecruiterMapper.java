package com.internhub.backend.mapper;

import com.internhub.backend.dto.business.RecruiterDTO;
import com.internhub.backend.entity.business.Recruiter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruiterMapper {

    RecruiterDTO mapRecruiterToRecruiterDTO(Recruiter recruiter);
}
