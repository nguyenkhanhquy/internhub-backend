package com.internhub.backend.repository;

import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, String> {

    @Query("SELECT t FROM Teacher t WHERE " +
            "(:query IS NULL OR :query = '' OR " +
            "    LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(t.user.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "    LOWER(t.teacherId) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ")")
    Page<Teacher> adminFindAllTeachers(String query, Pageable pageable);
}
