package com.internhub.backend.task;

import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.repository.AcademicYearRepository;
import com.internhub.backend.repository.CourseRepository;
import com.internhub.backend.util.AcademicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class CourseTask {

    private final CourseRepository courseRepository;
    private final AcademicYearRepository academicYearRepository;

    @Autowired
    public CourseTask(CourseRepository courseRepository, AcademicYearRepository academicYearRepository) {
        this.courseRepository = courseRepository;
        this.academicYearRepository = academicYearRepository;
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void finishCourse() {
        try {
            LocalDate now = LocalDate.now();

            AcademicYear currentYear = AcademicUtils.getCurrentAcademicYear(now, academicYearRepository);
            Semester currentSemester = AcademicUtils.getCurrentSemester(now);

            LocalDate semesterEndDate = getSemesterEndDate(currentSemester, currentYear);
            if (!now.isBefore(semesterEndDate)) {
                List<Course> courses = courseRepository.findByAcademicYearAndSemesterAndCourseStatusNot(
                        currentYear, currentSemester, Course.CourseStatus.FINALIZED
                );

                for (Course course : courses) {
                    course.setCourseStatus(Course.CourseStatus.FINALIZED);
                }

                courseRepository.saveAll(courses);
                log.info("Đã cập nhật các khóa học sang trạng thái ĐÃ KẾT THÚC cho kỳ {} năm học {}.",
                        currentSemester, currentYear.getName());
            } else {
                log.info("Chưa đến thời điểm kết thúc kỳ học {} năm học {}.", currentSemester, currentYear.getName());
            }

        } catch (Exception e) {
            log.error("Lỗi trong quá trình kết thúc khóa học: {}", e.getMessage(), e);
        }
    }

    private LocalDate getSemesterEndDate(Semester semester, AcademicYear academicYear) {
        String[] parts = academicYear.getName().split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Tên năm học không hợp lệ: " + academicYear);
        }

        int startYear = Integer.parseInt(parts[0]);
        int endYear = Integer.parseInt(parts[1]);

        return switch (semester) {
            case HK01 -> LocalDate.of(startYear, 12, 31);
            case HK02 -> LocalDate.of(endYear, 5, 31);
            case HK03 -> LocalDate.of(endYear, 7, 31);
        };
    }
}
