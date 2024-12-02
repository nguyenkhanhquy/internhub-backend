package com.internhub.backend.mapper;

import com.internhub.backend.dto.job.jobapply.JobApplyDetailDTO;
import com.internhub.backend.entity.job.JobApply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobApplyMapper {

    @Mapping(source = "jobPost.title", target = "title")
    @Mapping(source = "jobPost.jobPosition", target = "jobPosition")
    @Mapping(source = "jobPost.company", target = "company")
    @Mapping(source = "jobPost.expiryDate", target = "expiryDate")
    @Mapping(source = "student.name", target = "name")
    JobApplyDetailDTO toDetailDTO(JobApply jobApply);
}
