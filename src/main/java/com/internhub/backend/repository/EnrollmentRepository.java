package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    Page<Enrollment> findByStudent_UserIdAndCourse_CourseStatusNot(String userId, Course.CourseStatus courseCourseStatus, Pageable pageable);

    Page<Enrollment> findByStudent_UserIdAndCourse_CourseCodeContainingIgnoreCaseAndCourse_CourseStatusNot(String studentUserId, String courseCourseCode, Course.CourseStatus courseCourseStatus, Pageable pageable);

    Optional<Enrollment> findByStudentAndCourse_AcademicYear_Name_AndCourse_SemesterAndCourse_CourseStatus(
            Student student,
            @Param("academicYearName") String academicYearName,
            Semester semester,
            Course.CourseStatus courseStatus
    );

    Optional<Enrollment> findByStudentAndCourse_CourseCode(Student student, String courseCode);

    List<Enrollment> findAllByCourse(Course course);

    boolean existsByIdAndCourse_Teacher(String enrollmentId, Teacher teacher);
}
