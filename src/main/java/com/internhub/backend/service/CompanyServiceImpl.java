package com.internhub.backend.service;

import com.internhub.backend.dto.business.company.CompanyBasicDTO;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.mapper.CompanyMapper;
import com.internhub.backend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public SuccessResponse<List<CompanyBasicDTO>> getAllApprovedCompanies(int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Company> pageData = companyRepository.findAllApprovedCompanies(pageable);

        return SuccessResponse.<List<CompanyBasicDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(page)
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(companyMapper::mapCompanyToCompanyBasicDTO)
                        .toList())
                .build();
    }
}
