package com.internhub.backend.task;

import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.repository.AcademicYearRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Component
@Transactional
public class AcademicYearTask {

    private final AcademicYearRepository academicYearRepository;

    @Autowired
    public AcademicYearTask(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }

    @Scheduled(cron = "0 0 0 1 8 ?", zone = "Asia/Ho_Chi_Minh")
    public void createAcademicYear() {
        try {
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();
            if (currentMonth < 8) {
                currentYear--;
            }
            String academicYearName = currentYear + "-" + (currentYear + 1);

            if (academicYearRepository.existsByName(academicYearName)) {
                log.info("Năm học {} đã tồn tại, không tạo mới", academicYearName);
            } else {
                academicYearRepository.save(AcademicYear.builder()
                        .name(academicYearName)
                        .build());
                log.info("Đã hoàn tất nhiệm vụ tạo năm học mới: {}", academicYearName);
            }
        } catch (Exception e) {
            log.error("Lỗi trong quá trình tạo năm học mới: {}", e.getMessage(), e);
        }
    }
}
