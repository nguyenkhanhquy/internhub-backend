package com.internhub.backend.service;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.auth.LoginResponseDTO;
import com.internhub.backend.dto.auth.RefreshTokenDTO;
import com.internhub.backend.dto.request.auth.IntrospectRequest;
import com.internhub.backend.dto.request.auth.LoginRequest;
import com.internhub.backend.dto.request.auth.LogoutRequest;
import com.internhub.backend.dto.request.auth.RefreshTokenRequest;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.RecruiterMapper;
import com.internhub.backend.mapper.StudentMapper;
import com.internhub.backend.mapper.TeacherMapper;
import com.internhub.backend.mapper.UserMapper;
import com.internhub.backend.repository.RecruiterRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.repository.TeacherRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.util.AuthUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.valid-duration}")
    private int jwtValidDuration;

    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RecruiterMapper recruiterMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public LoginResponseDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_LOGIN);
        }

        if (!user.isActive()) {
            throw new CustomException(EnumException.USER_NOT_ACTIVATED);
        }

        if (user.isLocked()) {
            throw new CustomException(EnumException.USER_LOCKED);
        }

        String accessToken = tokenService.generateToken(user);
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(UUID.randomUUID().toString())
                .expirationTime(Instant.now().plus(jwtValidDuration, ChronoUnit.HOURS))
                .build();
    }

    @Override
    public Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String accessToken = introspectRequest.getAccessToken();

        try {
            SignedJWT signedJWT = tokenService.verifyToken(accessToken, false);

            // Lấy payload từ JWTClaimsSet
            Map<String, Object> payloadData = signedJWT.getJWTClaimsSet().getClaims();

            return Map.of("payload", payloadData);
        } catch (CustomException e) {
            throw new CustomException(EnumException.INVALID_TOKEN);
        }
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        String accessToken = logoutRequest.getAccessToken();

        SignedJWT signedJWT = tokenService.verifyToken(accessToken, false);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        tokenService.saveInvalidatedToken(jit, expiryTime);
    }

    @Override
    public RefreshTokenDTO refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        String accessToken = refreshTokenRequest.getAccessToken();

        SignedJWT signedJWT = tokenService.verifyToken(accessToken, true);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        tokenService.saveInvalidatedToken(jit, expiryTime);

        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        return RefreshTokenDTO.builder()
                .accessToken(tokenService.generateToken(user))
                .expirationTime(Instant.now().plus(jwtValidDuration, ChronoUnit.HOURS))
                .build();
    }

    @Override
    public UserDTO getCurrentAuthUser() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        return userMapper.mapUserToUserDTO(user);
    }

    @Override
    public Object getCurentAuthProfile() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        return switch (user.getRole().getName()) {
            case "STUDENT" -> studentMapper.toDTO(studentRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND)));
            case "RECRUITER" -> recruiterMapper.toDTO(recruiterRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND)));
            case "TEACHER" -> teacherMapper.toDTO(teacherRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND)));
            default -> throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        };
    }

    @Override
    public LoginResponseDTO outboundAuthenticate(String code) {
        String accessTokenGoogle = getOauthAccessTokenGoogle(code);

        User googleUser = getProfileDetailsGoogle(accessTokenGoogle);
        User user = userRepository.findByEmail(googleUser.getEmail());

        if (user == null) {
            throw new CustomException(EnumException.INVALID_LOGIN_GOOGLE);
        }

        if (!user.isActive()) {
            throw new CustomException(EnumException.USER_NOT_ACTIVATED);
        }

        if (user.isLocked()) {
            throw new CustomException(EnumException.USER_LOCKED);
        }

        String accessToken = tokenService.generateToken(user);
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(UUID.randomUUID().toString())
                .expirationTime(Instant.now().plus(jwtValidDuration, ChronoUnit.HOURS))
                .build();
    }

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

        return jsonObject.get("access_token").toString().replace("\"", "");
    }

    private User getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

        User user = new User();
        user.setEmail(jsonObject.get("email").toString().replace("\"", ""));

        return user;
    }
}
