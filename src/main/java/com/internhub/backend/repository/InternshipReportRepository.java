package com.internhub.backend.repository;

import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipReportRepository extends JpaRepository<InternshipReport, String> {

    Page<InternshipReport> findAllByStudent(Student student, Pageable pageable);
}
