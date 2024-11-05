package com.internhub.backend.mapper;

import com.internhub.backend.dto.business.company.CompanyBasicDTO;
import com.internhub.backend.entity.business.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyBasicDTO mapCompanyToCompanyBasicDTO(Company company);
}
