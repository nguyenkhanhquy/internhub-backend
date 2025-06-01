package com.internhub.backend.service;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.dto.request.students.UpdateStudentProfileRequest;
import com.internhub.backend.entity.academic.AcademicYear;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.Major;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.EnrollmentMapper;
import com.internhub.backend.repository.*;
import com.internhub.backend.util.AcademicUtils;
import com.internhub.backend.util.AuthUtils;
import com.internhub.backend.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, AcademicYearRepository academicYearRepository, EnrollmentRepository enrollmentRepository, RoleRepository roleRepository, UserRepository userRepository, EnrollmentMapper enrollmentMapper, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.academicYearRepository = academicYearRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.passwordEncoder = passwordEncoder;
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
    public EnrollmentDTO getCurrentEnrollmentByStudent() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        AcademicYear currentAcademicYear = AcademicUtils.getCurrentAcademicYear(now, academicYearRepository);
        Semester currentSemester = AcademicUtils.getCurrentSemester(now);

        Enrollment currentEnrollment = enrollmentRepository
                .findByStudentAndCourse_AcademicYear_Name_AndCourse_SemesterAndCourse_CourseStatus(
                        student,
                        currentAcademicYear.getName(),
                        currentSemester,
                        Course.CourseStatus.GRADING
                )
                .orElse(null);
        return enrollmentMapper.toDTO(currentEnrollment);
    }

    @Override
    public void importStudents(MultipartFile file) {
        if (ExcelUtils.isNotExcelFile(file)) {
            throw new CustomException(EnumException.FILE_TYPE_INVALID);
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            List<Student> students = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua tiêu đề

                String email = ExcelUtils.getCellValueAsString(row.getCell(0));
                String password = ExcelUtils.getCellValueAsString(row.getCell(1));
                String name = ExcelUtils.getCellValueAsString(row.getCell(2));
                String gender = ExcelUtils.getCellValueAsString(row.getCell(3));
                String phone = ExcelUtils.getCellValueAsString(row.getCell(4));
                String address = ExcelUtils.getCellValueAsString(row.getCell(5));
                String dob = ExcelUtils.getCellValueAsString(row.getCell(6));
                String expGrad = ExcelUtils.getCellValueAsString(row.getCell(7));
                String major = ExcelUtils.getCellValueAsString(row.getCell(8));
                String internStatus = ExcelUtils.getCellValueAsString(row.getCell(9));
                String studentId = ExcelUtils.getCellValueAsString(row.getCell(10));
                Double gpa = ExcelUtils.getCellValueAsDouble(row.getCell(11));

                User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .isActive(false)
                        .role(roleRepository.findByName("STUDENT"))
                        .build();

                User savedUser = userRepository.save(user);

                Student student = Student.builder()
                        .user(savedUser)
                        .name(name)
                        .gender(gender)
                        .phone(phone)
                        .address(address)
                        .dob(LocalDate.parse(dob))
                        .expGrad(LocalDate.parse(expGrad))
                        .major(Major.valueOf(major))
                        .internStatus(InternStatus.valueOf(internStatus))
                        .studentId(studentId)
                        .gpa(gpa)
                        .build();

                students.add(student);
            }

            studentRepository.saveAll(students);
        } catch (Exception e) {
            throw new CustomException(EnumException.IMPORT_FILE_ERROR);
        }
    }
}
