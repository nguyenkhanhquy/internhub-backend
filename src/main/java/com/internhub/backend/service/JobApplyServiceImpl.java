package com.internhub.backend.service;

import com.internhub.backend.dto.request.jobs.CreateJobApplyRequest;
import com.internhub.backend.entity.job.ApplyStatus;
import com.internhub.backend.entity.job.JobApply;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.repository.JobApplyRepository;
import com.internhub.backend.repository.JobPostRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JobApplyServiceImpl implements JobApplyService {

    private final JobApplyRepository jobApplyRepository;
    private final StudentRepository studentRepository;
    private final JobPostRepository jobPostRepository;

    @Override
    public void createJobApply(CreateJobApplyRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        JobApply jobApply = JobApply.builder()
                .jobPost(jobPost)
                .student(student)
                .coverLetter(request.getCoverLetter())
                .cv(request.getCv())
                .applyStatus(ApplyStatus.PENDING)
                .applyDate(Date.from(Instant.now()))
                .build();

        jobApplyRepository.save(jobApply);
    }
}
