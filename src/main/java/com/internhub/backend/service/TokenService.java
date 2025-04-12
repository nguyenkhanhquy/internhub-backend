package com.internhub.backend.service;

import com.internhub.backend.entity.account.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public interface TokenService {

    String generateToken(User user);

    SignedJWT verifyToken(String accessToken, boolean isRefresh) throws JOSEException;

    void saveInvalidatedToken(String tokenId, Date expiryTime);

    boolean isTokenInvalidated(String tokenId);
}
