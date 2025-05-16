package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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

    @Query("SELECT e.course FROM Enrollment e " +
            "WHERE e.student = :student " +
            "AND e.course.academicYear = :academicYear " +
            "AND e.course.semester = :semester " +
            "AND e.course.courseStatus = 'GRADING'")
    Optional<Course> findCurrentCourseByStudent(
            @Param("student") Student student,
            @Param("academicYear") AcademicYear academicYear,
            @Param("semester") Semester semester
    );

    Optional<Enrollment> findByStudentAndCourse_CourseCode(Student student, String courseCode);
}
