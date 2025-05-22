package com.internhub.backend.service;

import com.internhub.backend.dto.academic.YearAndSemesterDTO;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.mapper.SemesterMapper;
import com.internhub.backend.repository.AcademicYearRepository;
import com.internhub.backend.util.AcademicUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private final AcademicYearRepository academicYearRepository;
    private final SemesterMapper semesterMapper;

    @Override
    public YearAndSemesterDTO getAllYearAndSemester() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        return YearAndSemesterDTO.builder()
                .academicYears(academicYearRepository.findAll())
                .semesters(Arrays.stream(Semester.values())
                        .map(semesterMapper::toDTO)
                        .toList())
                .currentAcademicYear(AcademicUtils.getCurrentAcademicYear(now, academicYearRepository))
                .currentSemester(semesterMapper.toDTO(AcademicUtils.getCurrentSemester(now)))
                .build();
    }
}
