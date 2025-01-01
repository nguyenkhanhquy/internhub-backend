package com.internhub.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;

    private LocalDateTime expirationTime;
}
