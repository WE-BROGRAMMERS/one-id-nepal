package com.oneidnepal.auth_server.config.token;

import com.oneidnepal.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
@RequiredArgsConstructor
public class JwtTokenCustomizerConfig {

    private final UserRepository userRepository;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {

        return context -> {

            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

                String phoneNumber = context.getPrincipal().getName();

                userRepository.findByPhoneNumber(phoneNumber)
                        .ifPresent(user ->
                                context.getClaims().claim("userId", user.getId()));
            }
        };
    }
}