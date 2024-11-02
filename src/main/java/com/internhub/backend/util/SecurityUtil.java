package com.internhub.backend.util;

import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Authentication getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        return authentication;
    }
}
