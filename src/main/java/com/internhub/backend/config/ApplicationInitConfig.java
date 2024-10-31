package com.internhub.backend.config;

import com.internhub.backend.entity.User.Role;
import com.internhub.backend.entity.User.User;
import com.internhub.backend.repository.RoleRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.task.TokenCleanupTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final TokenCleanupTask tokenCleanupTask;

    @Autowired
    public ApplicationInitConfig(PasswordEncoder passwordEncoder, TokenCleanupTask tokenCleanupTask) {
        this.passwordEncoder = passwordEncoder;
        this.tokenCleanupTask = tokenCleanupTask;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            initializeRoles(roleRepository);
            initializeAdminUser(userRepository, roleRepository);
            runTokenCleanupTask();
        };
    }

    private void initializeRoles(RoleRepository roleRepository) {
        if (roleRepository.count() == 0) {
            List<String> roleNames = List.of("FIT", "STUDENT", "RECRUITER");
            roleNames.forEach(roleName ->
                    roleRepository.save(Role.builder()
                            .name(roleName)
                            .build())
            );
        }
    }

    private void initializeAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        if (userRepository.findByEmail("admin@admin.com") == null) {
            User user = User.builder()
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("12345678"))
                    .isActive(true)
                    .createdDate(Date.from(Instant.now()))
                    .updatedDate(Date.from(Instant.now()))
                    .role(roleRepository.findByName("FIT"))
                    .build();
            userRepository.save(user);
        }
    }

    private void runTokenCleanupTask() {
        log.info("Đang chạy tác vụ dọn dẹp token khi khởi động máy chủ...");
        tokenCleanupTask.deleteExpiredTokens();
    }
}
