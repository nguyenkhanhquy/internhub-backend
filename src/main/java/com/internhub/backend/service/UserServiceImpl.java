package com.internhub.backend.service;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.users.*;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.Major;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.UserMapper;
import com.internhub.backend.repository.*;
import com.internhub.backend.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RecruiterRepository recruiterRepository, CompanyRepository companyRepository, StudentRepository studentRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, OtpService otpService, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.recruiterRepository = recruiterRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
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
                    .createdDate(Date.from(Instant.now()))
                    .updatedDate(Date.from(Instant.now()))
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
            user.setUpdatedDate(Date.from(Instant.now()));

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
                    .isActive(true)
                    .createdDate(Date.from(Instant.now()))
                    .updatedDate(Date.from(Instant.now()))
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
                    .createdDate(Date.from(Instant.now()))
                    .updatedDate(Date.from(Instant.now()))
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
    public void sendOTP(Map<String, String> request) {
        String email = request.get("email");

        if (!userRepository.existsByEmail(email)) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        int otp = otpService.generateOtp(email);
        emailService.sendSimpleEmail(email, "Mã OTP", "Mã xác thực của bạn là: " + otp);
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
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Authentication authentication = SecurityUtil.getAuthenticatedUser();

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        user.setUpdatedDate(Date.from(Instant.now()));
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
