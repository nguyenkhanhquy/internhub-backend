package com.internhub.backend.dto.request.auth;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    private String accessToken;
}
