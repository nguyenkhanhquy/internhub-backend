package com.internhub.backend.mapper;

import com.internhub.backend.dto.job.jobpost.JobPostBasicDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.entity.job.JobPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPostMapper {

    JobPostBasicDTO mapJobPostToJobPostBasicDTO(JobPost jobPost);

    JobPostDetailDTO mapJobPostToJobPostDetailDTO(JobPost jobPost);
}
