package com.internhub.backend.service;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.users.*;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.Major;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.UserMapper;
import com.internhub.backend.repository.*;
import com.internhub.backend.util.AuthUtils;
import com.opencsv.CSVReader;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RecruiterRepository recruiterRepository, CompanyRepository companyRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, OtpService otpService, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.recruiterRepository = recruiterRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("getName() -> Email : {}", authentication.getName());
        log.info("getAuthorities() -> Scope : {}", authentication.getAuthorities());

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::mapUserToUserDTO)
                .toList();
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        return userMapper.mapUserToUserDTO(user);
    }

    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        try {
            User user = User.builder()
                    .email(createUserRequest.getEmail())
                    .password(passwordEncoder.encode(createUserRequest.getPassword()))
                    .isActive(true)
                    .build();

            User savedUser = userRepository.save(user);

            return userMapper.mapUserToUserDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.EMAIL_EXISTED);
        }
    }

    @Override
    public UserDTO updateUser(String id, UpdateUserRequest updateUserRequest) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

            user.setEmail(updateUserRequest.getEmail());
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));

            User updatedUser = userRepository.save(user);

            return userMapper.mapUserToUserDTO(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.EMAIL_EXISTED);
        }
    }

    @Override
    public UserDTO deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        if (user.getRole() == null) {
            userRepository.delete(user);
        } else {
            if (user.getRole().getName().equals("RECRUITER")) {
                recruiterRepository.deleteById(user.getId());
            } else if (user.getRole().getName().equals("STUDENT")) {
                studentRepository.deleteById(user.getId());
            } else {
                throw new CustomException(EnumException.ADMIN_CANNOT_BE_DELETED);
            }
            userRepository.delete(user);
        }

        return userMapper.mapUserToUserDTO(user);
    }

    @Override
    public UserDTO registerRecruiter(RegisterRecruiterRequest registerRecruiterRequest) {
        if (companyRepository.existsByName(registerRecruiterRequest.getCompany())) {
            throw new CustomException(EnumException.COMPANY_EXISTED);
        }

        try {
            User user = User.builder()
                    .email(registerRecruiterRequest.getEmail())
                    .password(passwordEncoder.encode(registerRecruiterRequest.getPassword()))
                    .isActive(false)
                    .role(roleRepository.findByName("RECRUITER"))
                    .build();

            User savedUser = userRepository.save(user);

            Company company = Company.builder()
                    .name(registerRecruiterRequest.getCompany())
                    .build();

            recruiterRepository.save(Recruiter.builder()
                    .user(savedUser)
                    .company(company)
                    .name(registerRecruiterRequest.getName())
                    .position(registerRecruiterRequest.getPosition())
                    .phone(registerRecruiterRequest.getPhone())
                    .recruiterEmail(registerRecruiterRequest.getRecruiterEmail())
                    .build()
            );

            return userMapper.mapUserToUserDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.EMAIL_EXISTED);
        }
    }

    @Override
    public UserDTO registerStudent(RegisterStudentRequest registerStudentRequest) {
        String email = registerStudentRequest.getEmail();

        // Kiểm tra phần sau dấu '@'
        String emailDomain = email.substring(email.indexOf("@") + 1);
        if (!"student.hcmute.edu.vn".equals(emailDomain)) {
            throw new CustomException(EnumException.INVALID_EMAIL_DOMAIN);
        }

        // Lấy phần trước '@'
        String extractedStudentId = email.substring(0, email.indexOf("@"));

        // Kiểm tra mã ngành trong mã sinh viên
        String majorCode = extractedStudentId.substring(3, 5); // Lấy 2 ký tự từ vị trí thứ 3
        if (!"10".equals(majorCode)) {
            throw new CustomException(EnumException.INVALID_MAJOR_CODE);
        }

        String studentId = registerStudentRequest.getStudentId();

        if (!extractedStudentId.equals(studentId)) {
            throw new CustomException(EnumException.EMAIL_AND_STUDENT_ID_MISMATCH);
        }

        try {
            User user = User.builder()
                    .email(registerStudentRequest.getEmail())
                    .password(passwordEncoder.encode(registerStudentRequest.getPassword()))
                    .isActive(false)
                    .role(roleRepository.findByName("STUDENT"))
                    .build();

            User savedUser = userRepository.save(user);

            studentRepository.save(Student.builder()
                    .user(savedUser)
                    .name(registerStudentRequest.getName())
                    .gender(registerStudentRequest.getGender())
                    .phone(registerStudentRequest.getPhone())
                    .address(registerStudentRequest.getAddress())
                    .dob(registerStudentRequest.getDob())
                    .expGrad(registerStudentRequest.getExpGrad())
                    .major(Major.valueOf(registerStudentRequest.getMajor()))
                    .internStatus(InternStatus.valueOf(registerStudentRequest.getInternStatus()))
                    .studentId(registerStudentRequest.getStudentId())
                    .gpa(registerStudentRequest.getGpa())
                    .build()
            );

            return userMapper.mapUserToUserDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.EMAIL_EXISTED);
        }
    }

    @Override
    public void importTeachersFromFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("text/csv") && !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
            throw new CustomException(EnumException.FILE_TYPE_INVALID);
        }

        List<Teacher> teachers = new ArrayList<>();

        try {
            if (contentType.equals("text/csv")) {
                // Xử lý file CSV
                CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
                List<String[]> rows = csvReader.readAll();
                for (String[] row : rows) {
                    if (row[0].equals("Name") && row[1].equals("Email")) continue; // Bỏ qua tiêu đề
                    try {
                        User user = User.builder()
                                .email(row[1])
                                .password(passwordEncoder.encode("12345678"))
                                .isActive(false)
                                .role(roleRepository.findByName("TEACHER"))
                                .build();

                        User savedUser = userRepository.save(user);

                        teachers.add(Teacher.builder()
                                .user(savedUser)
                                .name(row[0])
                                .email(row[1])
                                .build()
                        );
                    } catch (DataIntegrityViolationException e) {
                        log.warn("Email {} already exists, skipping this entry.", row[1]);
                    }
                }
            } else {
                // Xử lý file Excel
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Bỏ qua tiêu đề
                    String name = row.getCell(0).getStringCellValue();
                    String email = row.getCell(1).getStringCellValue();
                    try {
                        User user = User.builder()
                                .email(email)
                                .password(passwordEncoder.encode("12345678"))
                                .isActive(false)
                                .role(roleRepository.findByName("TEACHER"))
                                .build();

                        User savedUser = userRepository.save(user);

                        teachers.add(Teacher.builder()
                                .user(savedUser)
                                .name(name)
                                .email(email)
                                .build()
                        );
                    } catch (DataIntegrityViolationException e) {
                        log.warn("Email {} already exists, skipping this entry.", email);
                    }
                }
                workbook.close();
            }

            teacherRepository.saveAll(teachers);
        } catch (Exception e) {
            throw new CustomException(EnumException.IMPORT_FILE_ERROR);
        }
    }

    @Override
    public void sendOTP(Map<String, String> request) throws MessagingException {
        String email = request.get("email");

        if (!userRepository.existsByEmail(email)) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        int otp = otpService.generateOtp(email);

        String htmlContent =
                "<div style=\"display: flex; justify-content: center; align-items: center;\">" +
                        "    <div>" +
                        "        <h1>Xác thực địa chỉ email của bạn</h1>" +
                        "        <p style='font-size: 1.1em'>Để kích hoạt tài khoản InternHub, vui lòng xác thực rằng đây là địa chỉ email của bạn.</p>" +
                        "        <p style='display: inline-block; padding: 10px 20px; background-color: #28a745; color: #ffffff; text-decoration: none; border-radius: 5px;'>" + otp + "</p>" +
                        "        <p style='font-size: 1em; color: #888888;'>Mã OTP sẽ hết hạn sau 5 phút. Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>" +
                        "    </div>" +
                        "</div>";

        emailService.sendHtmlEmail(email, "InternHub - Xác thực địa chỉ email", htmlContent);
    }

    @Override
    public void activateAccount(Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        verifyOtp(email, otp);

        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void requestActivateAccount(Map<String, String> request) {
        String email = request.get("email");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        if (user.isActive()) {
            throw new CustomException(EnumException.ACCOUNT_ALREADY_ACTIVATED);
        }
    }

    @Override
    public void resetPassword(Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        verifyOtp(email, otp);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    private void verifyOtp(String email, String otpString) {
        try {
            int otp = Integer.parseInt(otpString);

            if (!otpService.validateOtp(email, otp)) {
                throw new CustomException(EnumException.INVALID_OTP);
            }
        } catch (NumberFormatException e) {
            throw new CustomException(EnumException.INVALID_OTP);
        }
    }
}
