package com.internhub.backend.mapper;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.entity.job.JobPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobPostMapper {

    JobPostBasicDTO mapJobPostToJobPostBasicDTO(JobPost jobPost);

    @Mapping(target = "saved", ignore = true)
    JobPostDetailDTO mapJobPostToJobPostDetailDTO(JobPost jobPost);
}
