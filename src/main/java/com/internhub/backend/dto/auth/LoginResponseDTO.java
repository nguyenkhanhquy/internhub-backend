package com.internhub.backend.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Builder
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;

    private Instant expirationTime;
}
