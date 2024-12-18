package com.internhub.backend.dto.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.internhub.backend.entity.business.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterDTO {

    private String userId;

    private String name;

    private String position;

    private String phone;

    private String recruiterEmail;

    private Company company;

    private boolean isApproved;
}
