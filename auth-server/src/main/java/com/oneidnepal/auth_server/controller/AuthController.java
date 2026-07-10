package com.oneidnepal.auth_server.controller;

import com.oneidnepal.auth_server.dto.LoginRequest;
import com.oneidnepal.auth_server.entity.User;
import com.oneidnepal.auth_server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Value("${spring.security.oauth2.authorizationserver.issuer:http://localhost:8080}")
    private String issuer;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request,
                                     HttpServletRequest httpRequest,
                                     HttpServletResponse httpResponse) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSessionSecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        contextRepository.saveContext(SecurityContextHolder.getContext(), httpRequest, httpResponse);

        String scope = request.getScope() == null || request.getScope().isBlank()
                ? "openid profile citizenship_data"
                : request.getScope().trim();

        User user = userRepository.findByPhoneNumber(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(5));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(user.getPhoneNumber())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("scope", scope)
                .build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "expires_in", Duration.between(issuedAt, expiresAt).getSeconds(),
                "scope", scope
        );
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> handleLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1. Perform local Spring Security clear out
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, auth);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();

            // 2. Determine caller context: Keycloak OIDC vs Angular App
            String postLogoutUri = request.getParameter("post_logout_redirect_uri");
            String state = request.getParameter("state");

            if (postLogoutUri != null && !postLogoutUri.isBlank()) {
                // Keycloak Flow: Needs a 302 Browser Redirect with state parameter
                String targetUrl = UriComponentsBuilder.fromUriString(postLogoutUri)
                        .queryParam("state", state)
                        .toUriString();

                response.sendRedirect(targetUrl);
                return null; // Return null as response handles redirection directly
            }

            // Angular Flow: Return structured JSON instead of redirecting
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Logged out successfully"
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Redirect failed: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Logout failed: " + e.getMessage()));
        }
    }
}
