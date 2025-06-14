package com.internhub.backend.task;

import com.internhub.backend.repository.InvalidatedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@Transactional
public class TokenCleanupTask {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    public TokenCleanupTask(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

//    @Scheduled(cron = "0 59 23 * * ?")
    public void deleteExpiredTokens() {
        log.info("Bắt đầu nhiệm vụ dọn dẹp token đã hết hạn tại {}", Instant.now());
        try {
            int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(Date.from(Instant.now()));
            log.info("Đã hoàn tất nhiệm vụ dọn dẹp token hết hạn. Số token đã xóa: {}", deletedCount);
        } catch (Exception e) {
            log.error("Lỗi trong quá trình dọn dẹp token: {}", e.getMessage(), e);
        }
    }
}
