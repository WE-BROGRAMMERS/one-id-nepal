package com.one_id_nepal.resource_server.api.controller;

import com.one_id_nepal.resource_server.api.dto.response.CitizenshipResponse;
import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;
import com.one_id_nepal.resource_server.api.service.PersonService;
import com.one_id_nepal.resource_server.person.entity.Person;
import com.one_id_nepal.resource_server.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/res/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/user")
    public ResponseEntity<PersonInfoResponse> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(personService.getUserInfo(jwt));
    }

    @GetMapping("/citizenship")
    public ResponseEntity<CitizenshipResponse> getCitizenshipInfo(@AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        return ResponseEntity.ok(personService.getCitizenshipInfo(jwt));
    }

    @GetMapping("/data")
    public ResponseEntity<Object> getUserData(@AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        return ResponseEntity.ok(personService.getUserData(jwt));
    }


}