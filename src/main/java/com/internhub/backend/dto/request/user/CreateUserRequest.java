package com.internhub.backend.dto.request.user;

import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String email;

    private String password;
}
