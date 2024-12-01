package com.internhub.backend.controller;

import com.internhub.backend.dto.business.company.CompanyBasicDTO;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/approved")
    public ResponseEntity<SuccessResponse<List<CompanyBasicDTO>>> getAllApprovedCompanies(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(companyService.getAllApprovedCompanies(page, size));
    }
}
