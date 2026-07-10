package com.one_id_nepal.resource_server.api.controller;

import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;
import com.one_id_nepal.resource_server.api.service.PersonService;
import com.one_id_nepal.resource_server.person.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/res/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/all")
    public ResponseEntity<List<PersonInfoResponse>> getAllPersons() {
        List<PersonInfoResponse> personInfoResponses = personService.getAllPersons();
        return ResponseEntity.ok(personInfoResponses);
    }

    @GetMapping("/me")
    public ResponseEntity<PersonInfoResponse> getPersonFromToken() {
        // Extract the authenticated user from the SecurityContext
        Person authenticatedPerson = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PersonInfoResponse personInfoResponse = new PersonInfoResponse(authenticatedPerson);
        return ResponseEntity.ok(personInfoResponse);
    }


}
