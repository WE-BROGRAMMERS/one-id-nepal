package com.one_id_nepal.resource_server.api.controller;

import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;
import com.one_id_nepal.resource_server.api.service.PersonService;
import com.one_id_nepal.resource_server.person.entity.Person;
import com.one_id_nepal.resource_server.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/res/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
//    private final PersonRepository personRepository;
//
//    @GetMapping("/all")
//    public ResponseEntity<List<PersonInfoResponse>> getAllPersons() {
//        return ResponseEntity.ok(personService.getAllPersons());
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<PersonInfoResponse> getPersonFromToken(
//            @AuthenticationPrincipal Jwt jwt) {
//
//        String userId = jwt.getClaimAsString("userId");
//
//        Person person = personRepository
//                .findByUserId(userId)
//                .orElseThrow(() ->
//                        new RuntimeException("Person not found"));
//
//        return ResponseEntity.ok(new PersonInfoResponse(person));
//    }

    @GetMapping("/user")
    public ResponseEntity<PersonInfoResponse> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(personService.getUserInfo(jwt));
    }
}