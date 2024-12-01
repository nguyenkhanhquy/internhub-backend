package com.internhub.backend.dto.business.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailDTO {

    private String id;

    private String name;

    private String website;

    private String description;

    private String address;

    private String logo;
}
