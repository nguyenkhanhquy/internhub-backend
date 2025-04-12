package com.internhub.backend.service;

import com.internhub.backend.entity.account.User;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.signerkey}")
    private String jwtSignerKey;
    @Value("${jwt.valid-duration}")
    private int jwtValidDuration;
    @Value("${jwt.refreshable-duration}")
    private int jwtRefreshableDuration;

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public TokenServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String generateToken(User user) {
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

    @Override
    public SignedJWT verifyToken(String accessToken, boolean isRefresh) throws JOSEException {
        try {
            // Tạo JWSVerifier với khóa ký bí mật
            JWSVerifier verifier = new MACVerifier(jwtSignerKey.getBytes());

            // Phân tích accessToken
            SignedJWT signedJWT = SignedJWT.parse(accessToken);

            // Kiểm tra tính hợp lệ của chữ ký
            boolean verified = signedJWT.verify(verifier);

            // Lấy thời gian hết hạn hoặc thời gian làm mới từ claims
            Instant expiration = (isRefresh)
                    ? signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(jwtRefreshableDuration, ChronoUnit.HOURS)
                    : signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

            // Kiểm tra thời gian hết hạn
            if (!(verified && Instant.now().isBefore(expiration))) {
                throw new CustomException(EnumException.INVALID_TOKEN);
            }

            // Kiểm tra xem token đã bị thu hồi chưa
            if (isTokenInvalidated(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new CustomException(EnumException.INVALID_TOKEN);
            }

            return signedJWT;
        } catch (ParseException e) {
            throw new CustomException(EnumException.INVALID_TOKEN);
        }
    }

    @Override
    public void saveInvalidatedToken(String tokenId, Date expiryTime) {
        Instant expiryInstant = expiryTime.toInstant();
        long ttlInMillis = Duration.between(Instant.now(), expiryInstant).toMillis();

        if (ttlInMillis > 0) {
            stringRedisTemplate.opsForValue().set(tokenId, "invalid", Duration.ofMillis(ttlInMillis));
        } else {
            log.warn("Token {} đã hết hạn, không lưu vào Redis.", tokenId);
        }
    }

    @Override
    public boolean isTokenInvalidated(String tokenId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(tokenId));
    }
}
