package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    long countStudentByIsReported(boolean isReported);

    long countByInternStatus(InternStatus internStatus);

    @Query("SELECT s FROM Student s WHERE " +
            "(:query IS NULL OR :query = '' OR " +
            "    LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(s.studentId) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ")")
    Page<Student> adminFindAllStudents(String query, Pageable pageable);

    List<Student> findByStudentId(String studentId);

    @Query("""
        SELECT s FROM Student s
        WHERE s.userId NOT IN (
            SELECT e.student.userId
            FROM Enrollment e
            WHERE e.course.academicYear.name = :academicYearName
              AND e.course.semester = :semester
        )
    """)
    List<Student> findStudentsNotEnrolledInSemester(
            @Param("academicYearName") String academicYearName,
            @Param("semester") Semester semester
    );
}
