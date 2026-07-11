package com.one_id_nepal.resource_server.api.service.service_impl;

import com.one_id_nepal.resource_server.api.dto.response.CitizenshipResponse;
import com.one_id_nepal.resource_server.api.dto.response.NidResponse;
import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;
import com.one_id_nepal.resource_server.api.service.PersonService;
import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import com.one_id_nepal.resource_server.nid.entity.NationalId;
import com.one_id_nepal.resource_server.person.entity.Person;
import com.one_id_nepal.resource_server.person.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public PersonInfoResponse getUserInfo(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getClaimAsString("userId");

        // Fetch the Person entity using the userId
        Person person = personRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Person not found for userId: " + userId));

        // Map the Person entity to PersonInfoResponse
        return new PersonInfoResponse(person);
    }

    @Override
    public CitizenshipResponse getCitizenshipInfo(Jwt jwt) throws AccessDeniedException {
        String userId = jwt.getClaimAsString("userId");
        String scope = jwt.getClaimAsString("scope");

        // Check if the scope includes citizenship_data
        if (scope == null || !scope.contains("openid profile citizenship_data")) {
            throw new AccessDeniedException("Insufficient scope to access citizenship data");
        }

        // Fetch the Person entity
        Person person = personRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found for userId: " + userId));

        // Fetch the Citizenship entity associated with the Person
        Citizenship citizenship = person.getCitizenship();
        if (citizenship == null) {
            throw new EntityNotFoundException("Citizenship not found for personId: " + person.getPersonId());
        }

        // Return the CitizenshipResponse
        return new CitizenshipResponse(citizenship, person);
    }

    @Override
    public Map<String, Object> getUserData(Jwt jwt) throws AccessDeniedException {
        String userId = jwt.getClaimAsString("userId");

        // Safely extract scopes natively as a List (handles the JSON array format
        // correctly)
        List<String> scopes = jwt.getClaimAsStringList("scope");
        if (scopes == null) {
            scopes = List.of();
        }

        boolean hasCitizenshipScope = scopes.contains("citizenship_data");
        boolean hasNidScope = scopes.contains("nid_data");

        // Reject early if neither required scope is present
        if (!hasCitizenshipScope && !hasNidScope) {
            throw new AccessDeniedException(
                    "Insufficient scope to access data. Required: citizenship_data or nid_data");
        }

        // Fetch the Person entity
        Person person = personRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found for userId: " + userId));

        // Dynamically build the response based on the scopes the client actually holds
        Map<String, Object> responseData = new HashMap<>();

        if (hasCitizenshipScope) {
            Citizenship citizenship = person.getCitizenship();
            if (citizenship == null) {
                throw new EntityNotFoundException("Citizenship data not found for personId: " + person.getPersonId());
            }
            responseData.put("citizenship", new CitizenshipResponse(citizenship, person));
        }

        if (hasNidScope) {
            NationalId nationalId = person.getNid();
            if (nationalId == null) {
                throw new EntityNotFoundException("National ID data not found for personId: " + person.getPersonId());
            }
            responseData.put("nid", new NidResponse(person, nationalId));
        }

        // Return a consistent JSON structure wrapped in "data"
        return Map.of("data", responseData);
    }
}
