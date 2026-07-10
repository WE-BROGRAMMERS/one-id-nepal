package com.oneidnepal.auth_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@Controller
public class ConsentController {

    private final RegisteredClientRepository clientRepository;
    private final OAuth2AuthorizationConsentService consentService;

    public ConsentController(
            RegisteredClientRepository clientRepository,
            OAuth2AuthorizationConsentService consentService) {

        this.clientRepository = clientRepository;
        this.consentService = consentService;
    }

    @GetMapping("/oauth2/consent")
    public String consent(
            Principal principal,
            @RequestParam("client_id") String clientId,
            @RequestParam("scope") String scopes,
            @RequestParam("state") String state,
            Model model) {

        RegisteredClient client =
                clientRepository.findByClientId(clientId);

        if (client == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown client_id");
        }

        Set<String> requestedScopes = new LinkedHashSet<>();
        String normalizedScopes = scopes.replace(',', ' ');
        String[] scopeTokens = StringUtils.delimitedListToStringArray(normalizedScopes, " ");
        for (String scope : scopeTokens) {
            if (StringUtils.hasText(scope)) {
                requestedScopes.add(scope.trim());
            }
        }

        OAuth2AuthorizationConsent previous =
                consentService.findById(client.getId(), principal.getName());

        Set<String> approved =
                previous != null
                        ? previous.getScopes()
                        : Collections.emptySet();

        Set<String> newScopes = new HashSet<>(requestedScopes);
        newScopes.removeAll(approved);

        model.addAttribute("clientName", client.getClientName());
        model.addAttribute("state", state);
        model.addAttribute("scopes", newScopes);

        Map<String, List<String>> scopeClaims =
                new LinkedHashMap<>();

        if (newScopes.contains("profile")) {
            scopeClaims.put(
                    "profile",
                    List.of("Name", "Citizenship Number")
            );
        }

        if (newScopes.contains("citizenship_data")) {
            scopeClaims.put(
                    "citizenship_data",
                    List.of("Citizenship Details", "Address")
            );
        }

        model.addAttribute("scopeClaims", scopeClaims);

        return "consent-new";
    }
}