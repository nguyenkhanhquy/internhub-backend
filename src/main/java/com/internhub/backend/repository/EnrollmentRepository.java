package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    Page<Enrollment> findByStudent_UserId(String userId, Pageable pageable);

    Page<Enrollment> findByStudent_UserIdAndCourse_CourseCodeContainingIgnoreCase(
            String userId, String courseCode, Pageable pageable
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

    List<Enrollment> findAllByCourse(Course course);

    boolean existsByIdAndCourse_Teacher(String enrollmentId, Teacher teacher);
}
