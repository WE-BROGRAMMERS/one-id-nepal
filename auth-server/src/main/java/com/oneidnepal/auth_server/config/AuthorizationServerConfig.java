package com.oneidnepal.auth_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    private final PasswordEncoder passwordEncoder;

    public AuthorizationServerConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {

        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {

        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {

        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);

        // Angular SPA Client
        if (repository.findByClientId("third-party-app") == null) {
            RegisteredClient spaClient = RegisteredClient.withId(UUID.randomUUID().toString())

                    .clientId("third-party-app")
                    .clientName("Third Party Demo App")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)

                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

                    .redirectUri("http://localhost:4200/callback")
                    .postLogoutRedirectUri( "http://localhost:4200/" )

                    .scope("openid")
                    .scope("profile")
                    .scope("citizenship_data")

                    .clientSettings(ClientSettings.builder()
                            .requireAuthorizationConsent(true)
                            .requireProofKey(true)
                            .build())

                    .tokenSettings(TokenSettings.builder()
                            .accessTokenTimeToLive(Duration.ofMinutes(15))
                            .refreshTokenTimeToLive(Duration.ofHours(8))
                            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                            .build())
                    .build();

            repository.save(spaClient);
        }

        // Keycloak Broker Client
        if (repository.findByClientId("keycloak") == null) {
            String clientSecret = "secret123"; // Change this to something strong in production

            RegisteredClient keycloakClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("keycloak")
                    .clientSecret(passwordEncoder.encode(clientSecret))   // Properly encoded with BCrypt

                    .clientName("Keycloak Broker")

                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)

                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

                    .redirectUri("http://localhost:8081/realms/NagarikLink/broker/oidc/endpoint")

                    .scope("openid")
                    .scope("profile")
                    .scope("citizenship_data")

                    .clientSettings(ClientSettings.builder()
                            .requireAuthorizationConsent(false)
                            .requireProofKey(false)
                            .build())

                    .tokenSettings(TokenSettings.builder()
                            .accessTokenTimeToLive(Duration.ofMinutes(15))
                            .refreshTokenTimeToLive(Duration.ofHours(8))
                            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                            .build())
                    .build();

            repository.save(keycloakClient);
            System.out.println("✅ Keycloak client registered with secret: " + clientSecret);
        }

        return repository;
    }
}