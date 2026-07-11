package com.oneidnepal.auth_server.config;

import com.oneidnepal.auth_server.developer.repository.DeveloperAppRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    /**
     * Raw JDBC-backed repository. Concrete return type so it can be injected
     * by type into the @Primary decorator and into DeveloperAppServiceImpl,
     * which deliberately bypasses the active-flag check to manage disabled apps.
     * Bean name: "jdbcRegisteredClientRepository" (no collision with the decorator below).
     */
    @Bean
    public JdbcRegisteredClientRepository jdbcRegisteredClientRepository(JdbcTemplate jdbcTemplate) {

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
                    .postLogoutRedirectUri("http://localhost:4200/")

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

        // eSewa Client
        if (repository.findByClientId("eSewa-app") == null) {
            RegisteredClient spaClient = RegisteredClient.withId(UUID.randomUUID().toString())

                    .clientId("eSewa-app")
                    .clientName("eSewa Demo App")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)

                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

                    .redirectUri("http://localhost:4202/callback")
                    .postLogoutRedirectUri("http://localhost:4202/")

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

        // Pathao Client
        if (repository.findByClientId("pathao-app") == null) {
            RegisteredClient pathaoClient = RegisteredClient.withId(UUID.randomUUID().toString())

                    .clientId("pathao-app")
                    .clientName("pathao Demo App")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)

                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

                    .redirectUri("http://localhost:4203/callback")
                    .postLogoutRedirectUri("http://localhost:4203/")

                    .scope("openid")
                    .scope("profile")
                    .scope("citizenship_data")
                    .scope("driving_license")

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

            repository.save(pathaoClient);
        }

        return repository;
    }

    /**
     * Primary repository seen by Spring Authorization Server and everything that
     * autowires the RegisteredClientRepository interface. Wraps the raw JDBC
     * repo so that apps marked inactive in developer_apps are invisible to the
     * OAuth authorize/token flow.
     */
    @Bean
    @Primary
    public RegisteredClientRepository registeredClientRepository(
            JdbcRegisteredClientRepository delegate,
            DeveloperAppRepository developerAppRepository) {
        return new ActiveAwareRegisteredClientRepository(delegate, developerAppRepository);
    }
}