package com.internhub.backend.service;

import com.internhub.backend.dto.academic.CourseDTO;
import com.internhub.backend.dto.request.students.UpdateStudentProfileRequest;
import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.CourseMapper;
import com.internhub.backend.repository.AcademicYearRepository;
import com.internhub.backend.repository.EnrollmentRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.AcademicUtils;
import com.internhub.backend.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, AcademicYearRepository academicYearRepository, EnrollmentRepository enrollmentRepository, CourseMapper courseMapper) {
        this.studentRepository = studentRepository;
        this.academicYearRepository = academicYearRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void updateStudentProfile(UpdateStudentProfileRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        student.setName(request.getName());
        student.setGender(request.getGender());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());
        student.setDob(request.getDob());
        student.setExpGrad(request.getExpGrad());
        student.setInternStatus(request.getInternStatus());
        student.setMajor(request.getMajor());
        student.setGpa(request.getGpa());

        studentRepository.save(student);
    }

    @Override
    public CourseDTO getCurrentCourseByStudent() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        AcademicYear currentAcademicYear = AcademicUtils.getCurrentAcademicYear(now, academicYearRepository);
        Semester currentSemester = AcademicUtils.getCurrentSemester(now);

        Course currentCourse = enrollmentRepository
                .findCurrentCourseByStudent(student, currentAcademicYear, currentSemester)
                .orElse(null);

        return courseMapper.toDTO(currentCourse);
    }
}
