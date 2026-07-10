package com.one_id_nepal.resource_server.config.security;

import com.one_id_nepal.resource_server.person.entity.Person;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenService {

    private final JwtDecoder jwtDecoder;

    public TokenService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Decode and validate the JWT.
     */
    private Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

    /**
     * Extract user id (JWT subject).
     */
    public String extractUserId(String token) {
        return extractClaim(token, Jwt::getSubject);
    }

    /**
     * Extract expiration.
     */
    public Date extractExpiration(String token) {
        Instant expiresAt = extractClaim(token, Jwt::getExpiresAt);
        return expiresAt != null ? Date.from(expiresAt) : null;
    }

    /**
     * Generic claim extractor.
     */
    public <T> T extractClaim(String token, Function<Jwt, T> claimsResolver) {
        Jwt jwt = decodeToken(token);
        return claimsResolver.apply(jwt);
    }

    /**
     * Extract any String claim.
     */
    public String extractStringClaim(String token, String claimName) {
        return decodeToken(token).getClaimAsString(claimName);
    }

    /**
     * Extract any claim.
     */
    public Object extractClaim(String token, String claimName) {
        return decodeToken(token).getClaim(claimName);
    }

    /**
     * Check whether token is expired.
     */
    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration == null || expiration.before(new Date());
    }

    /**
     * Validate token against Person.
     */
    public boolean validateToken(String token, Person person) {
        String userId = extractUserId(token);

        return userId != null
                && userId.equals(person.getUserId())
                && !isTokenExpired(token);
    }

    /**
     * Get decoded JWT.
     */
    public Jwt getJwt(String token) {
        return decodeToken(token);
    }
}