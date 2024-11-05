package com.internhub.backend.dto.request.jobs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostSearchFilterRequest {

    private int page = 1;
    private int size = 10;

    private String search;
    private String order = "latest";
}
