package com.one_id_nepal.resource_server.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/public/**",
                                "/v1/**",
                                "/uploads/**", // Allow access to uploads
                                "/one_id_nepal/**"
                        ).permitAll()

                        .requestMatchers("/api/res/citizen/**")
                        .hasAuthority("SCOPE_citizenship_data")

                        .requestMatchers("/api/res/person/data")
                        .hasAnyAuthority("SCOPE_citizenship_data", "SCOPE_nid_data", "SCOPE_profile", "SCOPE_openid")

                        .requestMatchers("/api/res/person/**")
                        .hasAuthority("SCOPE_profile")

                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return converter;
    }
}