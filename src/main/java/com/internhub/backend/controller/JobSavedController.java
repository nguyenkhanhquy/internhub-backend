package com.internhub.backend.controller;

import com.internhub.backend.dto.job.jobsaved.JobSavedDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.JobSavedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs/saved")
public class JobSavedController {

    private final JobSavedService jobSavedService;

    @Autowired
    public JobSavedController(JobSavedService jobSavedService) {
        this.jobSavedService = jobSavedService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobSavedDTO>>> getAllSavedJobPosts(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobSavedService.getAllSavedJobPosts(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSavedJobPosts() {
        jobSavedService.deleteAllSavedJobPosts();

        return ResponseEntity.noContent().build();
    }
}
