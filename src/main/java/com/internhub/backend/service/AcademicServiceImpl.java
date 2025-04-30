package com.internhub.backend.service;

import com.internhub.backend.dto.academic.YearAndSemesterDTO;
import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.mapper.SemesterMapper;
import com.internhub.backend.repository.AcademicYearRepository;
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
                .currentAcademicYear(getCurrentAcademicYear(now))
                .currentSemester(semesterMapper.toDTO(getCurrentSemester(now)))
                .build();
    }

    private AcademicYear getCurrentAcademicYear(LocalDate now) {
        int year = now.getYear();
        int month = now.getMonthValue();
        String name;
        if (month >= 8) {
            name = year + "-" + (year + 1);
        } else {
            name = (year - 1) + "-" + year;
        }
        return academicYearRepository.findByName(name);
    }

    private Semester getCurrentSemester(LocalDate now) {
        int month = now.getMonthValue();
        if (month >= 8) {
            return Semester.HK01;
        } else if (month <= 5) {
            return Semester.HK02;
        } else {
            return Semester.HK03;
        }
    }
}
