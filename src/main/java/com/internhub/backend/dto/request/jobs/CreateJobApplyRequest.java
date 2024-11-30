package com.internhub.backend.dto.request.jobs;

import lombok.Getter;

@Getter
public class CreateJobApplyRequest {

    private String jobPostId;

    private String coverLetter;

    private String cv;
}
