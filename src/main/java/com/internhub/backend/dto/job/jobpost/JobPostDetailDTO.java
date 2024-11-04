package com.internhub.backend.dto.job.jobpost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.job.JobApply;
import com.internhub.backend.entity.student.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostDetailDTO {

    private String id;

    private String title;

    private String type;

    private String remote;

    private String description;

    private String salary;

    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date expiryDate;

    private String jobPosition;

    private String requirements;

    private String benefits;

    private Company company;

    private String address;

    private List<JobApply> jobApplies;

    private List<Major> majors;
}
