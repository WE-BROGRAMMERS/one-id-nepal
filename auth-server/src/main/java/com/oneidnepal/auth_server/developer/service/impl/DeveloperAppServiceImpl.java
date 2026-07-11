package com.oneidnepal.auth_server.developer.service.impl;


import com.oneidnepal.auth_server.developer.dto.AppResponse;
import com.oneidnepal.auth_server.developer.dto.CreateAppRequest;
import com.oneidnepal.auth_server.developer.dto.DevLoginRequest;
import com.oneidnepal.auth_server.developer.dto.UpdateAppRequest;
import com.oneidnepal.auth_server.developer.entity.ClientType;
import com.oneidnepal.auth_server.developer.entity.DeveloperApp;
import com.oneidnepal.auth_server.developer.repository.DeveloperAppRepository;
import com.oneidnepal.auth_server.developer.service.DeveloperAppService;
import com.oneidnepal.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


/**
 * @author Utsab Dahal
 */
@Service
@RequiredArgsConstructor
public class DeveloperAppServiceImpl implements DeveloperAppService {

    private final JdbcRegisteredClientRepository registeredClientRepository;
    private final DeveloperAppRepository developerAppRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Value("${spring.security.oauth2.authorizationserver.issuer:http://localhost:8080}")
    private String issuer;

    private static final SecureRandom RNG = new SecureRandom();
    private static final char[] BASE62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    private static final Set<String> SELF_SERVICE_SCOPES = Set.of("openid", "profile", "email");
    private static final Set<String> RESTRICTED_SCOPES = Set.of("citizenship_data", "driving_license", "nid_data");

    @Override
    @Transactional
    public AppResponse createApp(String ownerUserId, CreateAppRequest request) {
        if (request.getAppName() == null || request.getAppName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "appName is required");
        }
        validateRedirectUris(request.getRedirectUris());
        Set<String> scopes = resolveScopes(request.getScopes());

        String clientId = generateUniqueClientId(request.getAppName());
        boolean confidential = request.getClientType() == ClientType.CONFIDENTIAL;
        String rawSecret = confidential ? generateSecret() : null;
        String registeredId = UUID.randomUUID().toString();

        RegisteredClient.Builder builder = RegisteredClient.withId(registeredId)
                .clientId(clientId)
                .clientName(request.getAppName())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(true)          // PKCE required for everyone, incl. confidential
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .refreshTokenTimeToLive(Duration.ofHours(8))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                        .build());

        if (confidential) {
            builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .clientSecret(passwordEncoder.encode(rawSecret));
        } else {
            builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
        }

        request.getRedirectUris().forEach(builder::redirectUri);
        if (request.getPostLogoutRedirectUris() != null) {
            request.getPostLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
        }
        scopes.forEach(builder::scope);

        registeredClientRepository.save(builder.build());

        DeveloperApp app = DeveloperApp.builder()
                .registeredClientId(registeredId)
                .ownerUserId(ownerUserId)
                .appName(request.getAppName())
                .clientId(clientId)
                .clientType(request.getClientType())
                .active(true)
                .build();
        developerAppRepository.save(app);

        return toResponse(app, rawSecret);   // secret returned ONCE, here
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppResponse> getMyApps(String ownerUserId) {
        return developerAppRepository.findByOwnerUserId(ownerUserId).stream()
                .map(a -> toResponse(a, null))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppResponse getApp(String ownerUserId, String appId) {
        return toResponse(requireOwnedApp(ownerUserId, appId), null);
    }

    @Override
    @Transactional
    public AppResponse updateApp(String ownerUserId, String appId, UpdateAppRequest request) {
        DeveloperApp app = requireOwnedApp(ownerUserId, appId);
        RegisteredClient existing = registeredClientRepository.findById(app.getRegisteredClientId());
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registered client missing");
        }
        validateRedirectUris(request.getRedirectUris());
        Set<String> scopes = resolveScopes(request.getScopes());

        RegisteredClient.Builder builder = RegisteredClient.from(existing)
                .redirectUris(Set::clear)
                .postLogoutRedirectUris(Set::clear)
                .scopes(Set::clear);
        if (request.getAppName() != null && !request.getAppName().isBlank()) {
            builder.clientName(request.getAppName());
        }
        request.getRedirectUris().forEach(builder::redirectUri);
        if (request.getPostLogoutRedirectUris() != null) {
            request.getPostLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
        }
        scopes.forEach(builder::scope);

        registeredClientRepository.save(builder.build());

        if (request.getAppName() != null && !request.getAppName().isBlank()) {
            app.setAppName(request.getAppName());
        }
        app.setActive(request.isActive());
        developerAppRepository.save(app);

        return toResponse(app, null);
    }

    @Override
    @Transactional
    public void deleteApp(String ownerUserId, String appId) {
        DeveloperApp app = requireOwnedApp(ownerUserId, appId);
        String rcId = app.getRegisteredClientId();
        // RegisteredClientRepository has no delete() — go direct, and clean up dependents.
        jdbcTemplate.update("DELETE FROM oauth2_authorization WHERE registered_client_id = ?", rcId);
        jdbcTemplate.update("DELETE FROM oauth2_authorization_consent WHERE registered_client_id = ?", rcId);
        jdbcTemplate.update("DELETE FROM oauth2_registered_client WHERE id = ?", rcId);
        developerAppRepository.delete(app);
    }

    @Override
    @Transactional
    public AppResponse rotateClientSecret(String ownerUserId, String appId) {
        DeveloperApp app = requireOwnedApp(ownerUserId, appId);
        if (!app.getClientType().equals(ClientType.CONFIDENTIAL)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Public (PKCE) clients have no secret to rotate");
        }
        RegisteredClient existing = registeredClientRepository.findById(app.getRegisteredClientId());
        String rawSecret = generateSecret();
        registeredClientRepository.save(
                RegisteredClient.from(existing)
                        .clientSecret(passwordEncoder.encode(rawSecret))
                        .build());
        return toResponse(app, rawSecret);
    }

    @Override
    public Map<String, Object> login(DevLoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getPhoneNumber(), req.getPassword())
        );

        String scope = req.getScope() == null || req.getScope().isBlank()
                ? "openid profile"
                : req.getScope().trim();

        var user = userRepository.findByPhoneNumber(authentication.getName())
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(5));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(user.getPhoneNumber())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("scope", scope)
                .claim("userId", user.getId())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "expires_in", Duration.between(issuedAt, expiresAt).getSeconds(),
                "expires_at", expiresAt.toString(),
                "scope", scope
        );
    }

    // ---- helpers ----

    private DeveloperApp requireOwnedApp(String ownerUserId, String appId) {
        DeveloperApp app = developerAppRepository.findById(appId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "App not found"));
        if (!app.getOwnerUserId().equals(ownerUserId)) {
            // 404 not 403 — don't leak that another owner's app exists
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "App not found");
        }
        return app;
    }

    private Set<String> resolveScopes(List<String> requested) {
        Set<String> scopes = new LinkedHashSet<>();
        scopes.add("openid");
        if (requested != null) {
            for (String s : requested) {
                if (s == null || s.isBlank()) continue;
                String scope = s.trim();
                if (RESTRICTED_SCOPES.contains(scope)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "Scope '" + scope + "' requires manual approval and cannot be self-assigned");
                }
                if (!SELF_SERVICE_SCOPES.contains(scope)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown scope: " + scope);
                }
                scopes.add(scope);
            }
        }
        return scopes;
    }

    private void validateRedirectUris(List<String> uris) {
        if (uris == null || uris.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one redirect URI is required");
        }
        for (String uri : uris) {
            URI parsed;
            try { parsed = URI.create(uri); }
            catch (Exception e) { throw badUri(uri); }
            if (!parsed.isAbsolute() || parsed.getFragment() != null) throw badUri(uri);
            boolean localhost = "localhost".equals(parsed.getHost()) || "127.0.0.1".equals(parsed.getHost());
            if (!"https".equalsIgnoreCase(parsed.getScheme()) && !localhost) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Redirect URIs must use https (http allowed only for localhost): " + uri);
            }
        }
    }

    private ResponseStatusException badUri(String uri) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid redirect URI: " + uri);
    }

    private String generateUniqueClientId(String appName) {
        String base = appName.toLowerCase().replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (base.isBlank()) base = "app";
        for (int i = 0; i < 5; i++) {
            String candidate = base + "-" + randomToken(6);
            if (developerAppRepository.findByClientId(candidate).isEmpty()
                    && registeredClientRepository.findByClientId(candidate) == null) {
                return candidate;
            }
        }
        return base + "-" + randomToken(12);
    }

    private String generateSecret() { return randomToken(48); }

    private String randomToken(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(BASE62[RNG.nextInt(BASE62.length)]);
        return sb.toString();
    }

    private AppResponse toResponse(DeveloperApp app, String rawSecret) {
        return AppResponse.builder()
                .id(app.getId())
                .appName(app.getAppName())
                .clientId(app.getClientId())
                .clientSecret(rawSecret)      // null except on create/rotate
                .clientType(app.getClientType())
                .active(app.isActive())
                .createdAt(app.getCreatedAt())
                .build();
    }
}