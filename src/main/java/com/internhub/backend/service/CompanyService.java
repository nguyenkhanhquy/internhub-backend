package com.internhub.backend.service;

import com.internhub.backend.dto.business.company.CompanyBasicDTO;
import com.internhub.backend.dto.business.company.CompanyDetailDTO;
import com.internhub.backend.dto.response.SuccessResponse;

import java.util.List;

public interface CompanyService {

    SuccessResponse<List<CompanyBasicDTO>> getAllApprovedCompanies(int page, int size);

    CompanyDetailDTO getCompanyById(String companyId);
}
