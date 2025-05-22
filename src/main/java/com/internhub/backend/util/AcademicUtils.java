package com.internhub.backend.util;

import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.repository.AcademicYearRepository;

import java.time.LocalDate;

public class AcademicUtils {

    private AcademicUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static AcademicYear getCurrentAcademicYear(LocalDate now, AcademicYearRepository academicYearRepository) {
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

    public static Semester getCurrentSemester(LocalDate now) {
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
