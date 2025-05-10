package com.internhub.backend.repository;

import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, String> {

    boolean existsByCourseCode(String courseCode);

    @Query("SELECT c FROM Course c " +
            "WHERE (:search IS NULL OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:year IS NULL OR c.academicYear.name = :year) " +
            "AND (:semester IS NULL OR c.semester = :semester) " +
            "ORDER BY c.startDate DESC")
    Page<Course> filterCourses(
            Pageable pageable,
            @Param("search") String search,
            @Param("year") String year,
            @Param("semester") Semester semester
    );
}
