package com.internhub.backend.repository;

import com.internhub.backend.entity.job.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPost, String> {

}
