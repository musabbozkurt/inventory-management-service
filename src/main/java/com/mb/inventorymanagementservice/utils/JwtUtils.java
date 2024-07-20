package com.mb.inventorymanagementservice.utils;

import com.mb.inventorymanagementservice.exception.BaseException;
import com.mb.inventorymanagementservice.exception.InventoryManagementServiceErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${testing.app.jwt-secret}")
    private String jwtSecret;

    @Value("${testing.app.jwt-expiration-ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            return StringUtils.isNotBlank(getUserNameFromJwtToken(authToken));
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token. validateJwtToken - Exception: {}", e.getMessage());
            throw new BaseException(InventoryManagementServiceErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired. validateJwtToken - Exception: {}", e.getMessage());
            throw new BaseException(InventoryManagementServiceErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported. validateJwtToken - Exception: {}", e.getMessage());
            throw new BaseException(InventoryManagementServiceErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty. validateJwtToken - Exception: {}", e.getMessage());
            throw new BaseException(InventoryManagementServiceErrorCode.INVALID_TOKEN);
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
