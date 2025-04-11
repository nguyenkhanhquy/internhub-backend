package com.internhub.backend.service;

import java.util.Date;

public interface TokenService {

    void saveInvalidatedToken(String tokenId, Date expiryTime);

    boolean isTokenInvalidated(String tokenId);
}
