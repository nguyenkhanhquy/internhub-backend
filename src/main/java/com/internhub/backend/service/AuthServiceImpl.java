package com.internhub.backend.service;

import com.internhub.backend.dto.request.auth.LoginRequest;
import com.internhub.backend.entity.User;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{

    @Value("${jwt.signerkey}")
    private String jwtSignerKey;

    @Value("${jwt.valid-duration}")
    private int jwtValidDuration;

    @Value("${jwt.refreshable-duration}")
    private int jwtRefreshableDuration;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_LOGIN);
        }

        String token = generateToken(user);
        return Map.of("accessToken", token);
    }

    private String generateToken(User user) {
        // Tạo JWSHeader với "typ": "JWT" và "alg": "HS256"
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        // Lấy thời gian hiện tại
        Instant now = Instant.now();
        // Tạo thời gian hết hạn (thời gian hiện tại + thời hạn token)
        Instant expiration = now.plus(jwtValidDuration, ChronoUnit.HOURS);

        // Tạo JWTClaimsSet chứa các thông tin cần thiết
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("internhub.com")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString())
//                .claim("scope", user.getRole().getName())
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
