package com.internhub.backend.repository;

import com.internhub.backend.entity.job.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, String> {

    List<JobPost> findByTitleContainingIgnoreCase(String title);

    Page<JobPost> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
