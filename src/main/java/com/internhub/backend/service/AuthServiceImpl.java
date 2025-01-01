package com.internhub.backend.service;

import com.internhub.backend.dto.LoginResponseDTO;
import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.auth.IntrospectRequest;
import com.internhub.backend.dto.request.auth.LoginRequest;
import com.internhub.backend.dto.request.auth.LogoutRequest;
import com.internhub.backend.dto.request.auth.RefreshTokenRequest;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.token.InvalidatedToken;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.RecruiterMapper;
import com.internhub.backend.mapper.StudentMapper;
import com.internhub.backend.mapper.UserMapper;
import com.internhub.backend.repository.InvalidatedTokenRepository;
import com.internhub.backend.repository.RecruiterRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.util.AuthUtils;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.signerkey}")
    private String jwtSignerKey;
    @Value("${jwt.valid-duration}")
    private int jwtValidDuration;
    @Value("${jwt.refreshable-duration}")
    private int jwtRefreshableDuration;
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RecruiterMapper recruiterMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, InvalidatedTokenRepository invalidatedTokenRepository, RecruiterRepository recruiterRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RecruiterMapper recruiterMapper, StudentMapper studentMapper) {
        this.userRepository = userRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.recruiterRepository = recruiterRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.recruiterMapper = recruiterMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_LOGIN);
        }

        if (!user.isActive()) {
            throw new CustomException(EnumException.USER_NOT_ACTIVATED);
        }

        String accessToken = generateToken(user);
        return Map.of("accessToken", accessToken);
    }

    @Override
    public Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String accessToken = introspectRequest.getAccessToken();

        try {
            SignedJWT signedJWT = verifyToken(accessToken, false);

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

        SignedJWT signedJWT = verifyToken(accessToken, false);
        invalidatedTokenRepository.save(createInvalidatedToken(signedJWT));
    }

    @Override
    public Map<String, Object> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        String accessToken = refreshTokenRequest.getAccessToken();

        SignedJWT signedJWT = verifyToken(accessToken, true);
        invalidatedTokenRepository.save(createInvalidatedToken(signedJWT));

        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        String newAccessToken = generateToken(user);
        return Map.of("accessToken", newAccessToken);
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

        if (user.getRole().getName().equals("STUDENT")) {
            return studentMapper.mapStudentToStudentDTO(studentRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND)));
        } else if (user.getRole().getName().equals("RECRUITER")) {
            return recruiterMapper.toDTO(recruiterRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND)));
        } else {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }
    }

    @Override
    public LoginResponseDTO outboundAuthenticate(String code) {
        String accessToken = getOauthAccessTokenGoogle(code);

        User googleUser = getProfileDetailsGoogle(accessToken);
        User user = userRepository.findByEmail(googleUser.getEmail());

        if (user == null) {
            throw new CustomException(EnumException.INVALID_LOGIN);
        }

        if (!user.isActive()) {
            throw new CustomException(EnumException.USER_NOT_ACTIVATED);
        }

        return LoginResponseDTO.builder()
                .accessToken(generateToken(user))
                .refreshToken(UUID.randomUUID().toString())
                .expirationTime(LocalDateTime.now().plusHours(jwtValidDuration))
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
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
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

    private InvalidatedToken createInvalidatedToken(SignedJWT signedJWT) throws ParseException {
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
    }

    private SignedJWT verifyToken(String accessToken, boolean isRefresh) throws JOSEException {
        try {
            JWSVerifier verifier = new MACVerifier(jwtSignerKey.getBytes());

            // Phân tích accessToken
            SignedJWT signedJWT = SignedJWT.parse(accessToken);

            // Kiểm tra tính hợp lệ của chữ ký
            boolean verified = signedJWT.verify(verifier);

            // Lấy thời gian hết hạn hoặc thời gian làm mới từ claims
            Instant expiration = (isRefresh)
                    ? signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(jwtRefreshableDuration, ChronoUnit.HOURS)
                    : signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

            if (!(verified && Instant.now().isBefore(expiration))) {
                throw new CustomException(EnumException.INVALID_TOKEN);
            }

            if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new CustomException(EnumException.INVALID_TOKEN);
            }

            return signedJWT;
        } catch (ParseException e) {
            throw new CustomException(EnumException.INVALID_TOKEN);
        }
    }

    private String generateToken(User user) {
        // Tạo JWSHeader với "typ": "JWT" và "alg": "HS256"
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        // Lấy thời gian hiện tại
        Instant now = Instant.now();
        // Tạo thời gian hết hạn (thời gian hiện tại + thời gian hiệu lực của accessToken)
        Instant expiration = now.plus(jwtValidDuration, ChronoUnit.HOURS);

        // Tạo JWTClaimsSet chứa các thông tin cần thiết
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("internhub.works")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole().getName())
                .claim("userId", user.getId())
                .build();

        // Chuyển đổi JWTClaimsSet thành Payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo JWSObject với Header và Payload
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            // Ký JWSObject với khóa ký bí mật
            jwsObject.sign(new MACSigner(jwtSignerKey.getBytes()));
            // Trả về JWT đã ký dưới dạng chuỗi
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new CustomException(EnumException.JWT_SIGNING_ERROR);
        }
    }
}
