package com.internhub.backend.repository;

import com.internhub.backend.entity.job.JobApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplyRepository extends JpaRepository<JobApply, String> {

}
