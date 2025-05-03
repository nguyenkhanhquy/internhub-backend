package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    @Query("SELECT e FROM Enrollment e " +
            "WHERE e.student = :student " +
            "AND LOWER(e.course.courseCode) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "ORDER BY e.updatedDate DESC")
    Page<Enrollment> filterEnrollmentsByStudent(
            @Param("student") Student student,
            Pageable pageable,
            @Param("search") String search
    );
}
