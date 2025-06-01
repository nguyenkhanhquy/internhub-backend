package com.internhub.backend.service;

import com.internhub.backend.dto.academic.OverviewDTO;
import com.internhub.backend.dto.academic.YearAndSemesterDTO;

public interface AcademicService {

    YearAndSemesterDTO getAllYearAndSemester();

    OverviewDTO getOverview();
}
