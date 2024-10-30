package com.internhub.backend.service;

import com.internhub.backend.dto.request.auth.IntrospectRequest;
import com.internhub.backend.dto.request.auth.LoginRequest;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginRequest loginRequest);

    Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
}
