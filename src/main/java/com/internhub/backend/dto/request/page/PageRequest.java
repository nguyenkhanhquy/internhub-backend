package com.internhub.backend.dto.request.page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {

    // pagination
    private int page = 1;
    private int size = 10;

    // search
    private String search;

    // sort
    private String order = "latest";
}
