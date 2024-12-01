package com.internhub.backend.dto.job.jobapply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.job.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplyDetailDTO {

    private String id;

    private String title;

    private String jobPosition;

    private Company company;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date applyDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expiryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ApplyStatus applyStatus;

    private String coverLetter;

    private String cv;

    private String interviewLetter;
}
