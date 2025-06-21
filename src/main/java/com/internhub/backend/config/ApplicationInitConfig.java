package com.internhub.backend.config;

import com.internhub.backend.entity.account.Role;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.repository.RoleRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.task.AcademicYearTask;
import com.internhub.backend.task.CourseTask;
import com.internhub.backend.task.TokenCleanupTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;
    private final AcademicYearTask academicYearTask;
    private final TokenCleanupTask tokenCleanupTask;
    private final CourseTask courseTask;

    @Autowired
    public ApplicationInitConfig(PasswordEncoder passwordEncoder, AcademicYearTask academicYearTask, TokenCleanupTask tokenCleanupTask, CourseTask courseTask) {
        this.passwordEncoder = passwordEncoder;
        this.academicYearTask = academicYearTask;
        this.tokenCleanupTask = tokenCleanupTask;
        this.courseTask = courseTask;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            initializeRoles(roleRepository);
            initializeAdminUser(userRepository, roleRepository);
            runCreateAcademicYearTask();
//            runTokenCleanupTask();
            runCourseTask();
        };
    }

    private void initializeRoles(RoleRepository roleRepository) {
        if (roleRepository.count() == 0) {
            List<String> roleNames = List.of("FIT", "STUDENT", "RECRUITER", "TEACHER");
            roleNames.forEach(roleName ->
                    roleRepository.save(Role.builder()
                            .name(roleName)
                            .build())
            );
            log.info("Các vai trò mặc định đã được tạo khi khởi động máy chủ");
        }
    }

    private void initializeAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        if (!userRepository.existsByEmail(adminEmail)) {
            User user = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .isActive(true)
                    .role(roleRepository.findByName("FIT"))
                    .build();
            userRepository.save(user);
            log.info("Tài khoản admin mặc định đã được tạo khi khởi động máy chủ");
        }
    }

    private void runCreateAcademicYearTask() {
        log.info("Đang chạy tác vụ tạo năm học mới khi khởi động máy chủ");
        academicYearTask.createAcademicYear();
    }

    private void runTokenCleanupTask() {
        log.info("Đang chạy tác vụ dọn dẹp token khi khởi động máy chủ");
        tokenCleanupTask.deleteExpiredTokens();
    }

    private void runCourseTask() {
        log.info("Đang chạy tác vụ xử lý khóa học khi khởi động máy chủ");
        courseTask.finishCourse();
    }
}
