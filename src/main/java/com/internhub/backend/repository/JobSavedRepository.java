package com.internhub.backend.repository;

import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.job.JobSaved;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSavedRepository extends JpaRepository<JobSaved, String> {

    boolean existsByStudentAndJobPost(Student student, JobPost jobPost);
}
