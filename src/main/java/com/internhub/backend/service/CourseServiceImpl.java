package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.academic.EnrollmentDetailDTO;
import com.internhub.backend.dto.request.courses.CreateCourseRequest;
import com.internhub.backend.dto.request.courses.UpdateCourseRequest;
import com.internhub.backend.dto.student.StudentDTO;
import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.CourseMapper;
import com.internhub.backend.mapper.EnrollmentMapper;
import com.internhub.backend.mapper.StudentMapper;
import com.internhub.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final AcademicYearRepository academicYearRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;
    private final EnrollmentMapper enrollmentMapper;

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
    public Page<CourseDTO> getAllCourses(Pageable pageable, String search, String year, String semesterValue) {
        Semester semester = Optional.ofNullable(semesterValue)
                .filter(s -> !s.isBlank())
                .map(s -> {
                    try {
                        return Semester.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .orElse(null);

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

    @Override
    public List<StudentDTO> getAllStudentsByCourseId(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(EnumException.COURSE_NOT_FOUND));

        List<Enrollment> enrollments = course.getEnrollments();

        return enrollments.stream()
                .map(Enrollment::getStudent)
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    public void assignStudentsToCourse(String courseId, List<String> studentIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(EnumException.COURSE_NOT_FOUND));

        // 1. Map nhanh để tìm studentId trong danh sách truyền vào
        Set<String> incomingStudentIdSet = new HashSet<>(studentIds);

        // 2. Lọc ra những enrollment hiện tại KHÔNG có trong danh sách mới => XÓA
        course.getEnrollments().removeIf(enrollment ->
                !incomingStudentIdSet.contains(enrollment.getStudent().getStudentId())
        );

        // 3. Thêm sinh viên mới chưa có
        for (String studentId : studentIds) {
            // Nếu đã có thì skip
            boolean alreadyEnrolled = course.getEnrollments().stream()
                    .anyMatch(e -> e.getStudent().getStudentId().equals(studentId));
            if (alreadyEnrolled) continue;

            Optional<Student> optionalStudent = studentRepository.findByStudentId(studentId)
                    .stream()
                    .findFirst();

            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                Enrollment enrollment = Enrollment.builder()
                        .course(course)
                        .student(student)
                        .build();
                course.getEnrollments().add(enrollment);
            }
        }

        courseRepository.save(course);
    }

    @Override
    public List<EnrollmentDetailDTO> getAllEnrollmentsByCourseId(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(EnumException.COURSE_NOT_FOUND));

        return enrollmentRepository.findAllByCourse(course)
                .stream()
                .map(enrollmentMapper::toDetailDTO)
                .toList();
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
