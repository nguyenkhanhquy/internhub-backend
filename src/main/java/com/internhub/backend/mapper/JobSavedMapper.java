package com.internhub.backend.mapper;

import com.internhub.backend.dto.job.jobsaved.JobSavedDTO;
import com.internhub.backend.entity.job.JobSaved;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobSavedMapper {

    @Mapping(source = "jobPost.id", target = "id")
    @Mapping(source = "jobPost.title", target = "title")
    @Mapping(source = "jobPost.jobPosition", target = "jobPosition")
    @Mapping(source = "jobPost.company.name", target = "companyName")
    @Mapping(source = "jobPost.expiryDate", target = "expiryDate")
    JobSavedDTO mapJobSavedToJobSavedDTO(JobSaved jobSaved);
}
