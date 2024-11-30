package com.internhub.backend.dto.request.jobs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostSearchFilterRequest {

    // pagination
    private int page = 1;
    private int size = 10;

    // search
    private String search;

    // sort
    private String order = "latest";

    // filter
    private Boolean isApproved;
    private Boolean isHidden;
    private Boolean isDeleted;
}
