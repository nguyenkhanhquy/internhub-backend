package com.internhub.backend.service;

import com.internhub.backend.dto.business.RecruiterDTO;
import com.internhub.backend.dto.request.recruiters.UpdateRecruiterProfileRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RecruiterService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    List<RecruiterDTO> getAllRecruiters();

    @PostAuthorize("returnObject.userId == authentication.principal.claims['userId'] or hasAuthority('SCOPE_FIT')")
    RecruiterDTO getRecruiterById(String id);

    void updateRecruiterProfile(UpdateRecruiterProfileRequest request);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void approveRecruiter(String id);
}
