package com.internhub.backend.dto.academic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.internhub.backend.entity.academic.AcademicYear;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class YearAndSemesterDTO {

    private List<AcademicYear> academicYears;

    private List<SemesterDTO> semesters;
}
