package com.internhub.backend.repository;

import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InternshipReportRepository extends JpaRepository<InternshipReport, String> {

    @Query("SELECT i FROM InternshipReport i WHERE " +
            "(:query IS NULL OR :query = '' OR " +
            "    LOWER(i.companyName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(i.student.studentId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(i.student.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ")")
    Page<InternshipReport> adminFindAllInternshipReports(String query, Pageable pageable);

    Page<InternshipReport> findAllByStudent(Student student, Pageable pageable);
}
