package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.courses.CreateCourseRequest;
import com.internhub.backend.dto.request.courses.UpdateCourseRequest;
import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.CourseMapper;
import com.internhub.backend.repository.AcademicYearRepository;
import com.internhub.backend.repository.CourseRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final AcademicYearRepository academicYearRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseDTO createCourse(CreateCourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new CustomException(EnumException.COURSE_CODE_EXISTS);
        }

        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new CustomException(EnumException.ACADEMIC_YEAR_NOT_FOUND));

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .courseName(request.getCourseName())
                .academicYear(academicYear)
                .semester(Semester.valueOf(request.getSemester()))
                .teacher(teacherRepository.findByTeacherId(request.getTeacherId()))
                .startDate(generateStartDate(academicYear.getName(), Semester.valueOf(request.getSemester())))
                .endDate(generateEndDate(academicYear.getName(), Semester.valueOf(request.getSemester())))
                .build();

        List<String> studentIds = request.getStudentIds();
        if (studentIds != null && !studentIds.isEmpty()) {
            for (String studentId : studentIds) {
                Student student = studentRepository.findByStudentId(studentId).getFirst();
                if (student != null) {
                    Enrollment enrollment = Enrollment.builder()
                            .course(course)
                            .student(student)
                            .build();
                    course.getEnrollments().add(enrollment);
                }
            }
        }

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDTO(savedCourse);
    }

    @Override
    public Page<CourseDTO> getAllCourses(Pageable pageable, String search ,String year, String semesterValue) {
        Semester semester = null;
        if (semesterValue != null && !semesterValue.isBlank()) {
            try {
                semester = Semester.valueOf(semesterValue);
            } catch (IllegalArgumentException ignored) {

            }
        }

        return courseRepository.filterCourses(pageable, search, year, semester)
                .map(courseMapper::toDTO);
    }

    @Override
    public CourseDTO getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(EnumException.COURSE_NOT_FOUND));

        return courseMapper.toDTO(course);
    }

    @Override
    public CourseDTO updateCourse(String courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(EnumException.COURSE_NOT_FOUND));

        if (!request.getCourseCode().equals(course.getCourseCode()) && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new CustomException(EnumException.COURSE_CODE_EXISTS);
        }

        if (!request.getSemester().equals(course.getSemester().name()) || !request.getAcademicYearId().equals(course.getAcademicYear().getId())) {
            course.setStartDate(generateStartDate(course.getAcademicYear().getName(), Semester.valueOf(request.getSemester())));
            course.setEndDate(generateEndDate(course.getAcademicYear().getName(), Semester.valueOf(request.getSemester())));
        }

        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setAcademicYear(academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new CustomException(EnumException.ACADEMIC_YEAR_NOT_FOUND)));
        course.setSemester(Semester.valueOf(request.getSemester()));
        course.setTeacher(teacherRepository.findByTeacherId(request.getTeacherId()));

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDTO(savedCourse);
    }

    @Override
    public void deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(EnumException.COURSE_NOT_FOUND));

        courseRepository.delete(course);
    }

    private LocalDate generateStartDate(String academicYear, Semester semester) {
        String[] years = academicYear.split("-");
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        switch (semester) {
            case HK01:
                return LocalDate.of(startYear, 8, 1);
            case HK02:
                return LocalDate.of(endYear, 1, 1);
            case HK03:
                return LocalDate.of(endYear, 6, 1);
            default:
                throw new IllegalArgumentException("Học kỳ không hợp lệ");
        }
    }

    private LocalDate generateEndDate(String academicYear, Semester semester) {
        String[] years = academicYear.split("-");
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        switch (semester) {
            case HK01:
                return LocalDate.of(startYear, 12, 31);
            case HK02:
                return LocalDate.of(endYear, 5, 31);
            case HK03:
                return LocalDate.of(endYear, 7, 31);
            default:
                throw new IllegalArgumentException("Học kỳ không hợp lệ");
        }
    }
}
