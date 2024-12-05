package com.internhub.backend.repository;

import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {

    long countStudentByIsReported(boolean isReported);

    long countByInternStatus(InternStatus internStatus);
}
