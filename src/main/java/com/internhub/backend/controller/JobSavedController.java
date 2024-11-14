package com.internhub.backend.controller;

import com.internhub.backend.service.JobSavedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs/save")
public class JobSavedController {

    private final JobSavedService jobSavedService;

    @Autowired
    public JobSavedController(JobSavedService jobSavedService) {
        this.jobSavedService = jobSavedService;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSavedJobPosts() {
        jobSavedService.deleteAllSavedJobPosts();

        return ResponseEntity.noContent().build();
    }
}
