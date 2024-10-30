package com.internhub.backend.config;

import com.internhub.backend.task.TokenCleanupTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    private final TokenCleanupTask tokenCleanupTask;

    @Autowired
    public ApplicationInitConfig(TokenCleanupTask tokenCleanupTask) {
        this.tokenCleanupTask = tokenCleanupTask;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> runTokenCleanupTask();
    }

    private void runTokenCleanupTask() {
        log.info("Running token cleanup task on startup...");
        tokenCleanupTask.deleteExpiredTokens();
    }
}
