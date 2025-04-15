package com.internhub.backend.service;

import com.internhub.backend.dto.academic.YearAndSemesterDTO;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.mapper.SemesterMapper;
import com.internhub.backend.repository.AcademicYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private final AcademicYearRepository academicYearRepository;
    private final SemesterMapper semesterMapper;

    @Override
    public YearAndSemesterDTO getAllYearAndSemester() {
        return YearAndSemesterDTO.builder()
                .academicYears(academicYearRepository.findAll())
                .semesters(Arrays.stream(Semester.values())
                        .map(semesterMapper::toDTO)
                        .toList())
                .build();
    }
}
