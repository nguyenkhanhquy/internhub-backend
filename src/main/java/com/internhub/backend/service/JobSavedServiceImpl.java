package com.internhub.backend.service;

import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.repository.JobSavedRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class JobSavedServiceImpl implements JobSavedService {

    private final JobSavedRepository jobSavedRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public JobSavedServiceImpl(JobSavedRepository jobSavedRepository, StudentRepository studentRepository) {
        this.jobSavedRepository = jobSavedRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void deleteAllSavedJobPosts() {
        Authentication authentication = SecurityUtil.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        jobSavedRepository.deleteAllByStudent(student);
    }
}
