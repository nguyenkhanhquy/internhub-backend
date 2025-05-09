package com.internhub.backend.service;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.auth.LoginResponseDTO;
import com.internhub.backend.dto.auth.RefreshTokenDTO;
import com.internhub.backend.dto.request.auth.IntrospectRequest;
import com.internhub.backend.dto.request.auth.LoginRequest;
import com.internhub.backend.dto.request.auth.LogoutRequest;
import com.internhub.backend.dto.request.auth.RefreshTokenRequest;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.Map;

public interface AuthService {

    LoginResponseDTO login(LoginRequest loginRequest);

    Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    RefreshTokenDTO refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    UserDTO getCurrentAuthUser();

    Object getCurentAuthProfile();

    LoginResponseDTO outboundAuthenticate(String code);
}
