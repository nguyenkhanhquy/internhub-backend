package com.internhub.backend.repository;

import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.job.JobSaved;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSavedRepository extends JpaRepository<JobSaved, String> {

    JobSaved findByStudentAndJobPost(Student student, JobPost jobPost);

    boolean existsByStudentAndJobPost(Student student, JobPost jobPost);

    List<JobSaved> findByStudent(Student student);

    void deleteAllByStudent(Student student);
}
