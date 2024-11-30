package com.internhub.backend.repository;

import com.internhub.backend.entity.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, String> {

}
