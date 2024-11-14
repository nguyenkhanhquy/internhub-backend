package com.internhub.backend.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

public interface JobSavedService {

    @PreAuthorize("hasAuthority('SCOPE_STUDENT')")
    @Transactional
    void deleteAllSavedJobPosts();
}
