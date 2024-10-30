package com.internhub.backend.repository;

import com.internhub.backend.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    int deleteByExpiryTimeBefore(Date now);
}
