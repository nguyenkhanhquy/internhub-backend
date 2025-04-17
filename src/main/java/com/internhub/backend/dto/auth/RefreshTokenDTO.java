package com.internhub.backend.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Builder
public class RefreshTokenDTO {

    private String accessToken;

    private Instant expirationTime;
}
