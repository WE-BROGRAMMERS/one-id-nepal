package com.one_id_nepal.resource_server.api.service.service_impl;

import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;
import com.one_id_nepal.resource_server.api.service.PersonService;
import com.one_id_nepal.resource_server.person.entity.Person;
import com.one_id_nepal.resource_server.person.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

//    @Override
//    public List<PersonInfoResponse> getAllPersons() {
//        log.info("Fetching all persons from the database");
//        return personRepository.findAll()
//                .stream()
//                .map(this::mapToPersonInfoResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    private PersonInfoResponse mapToPersonInfoResponse(Person person) {
//        return new PersonInfoResponse(
//                person.getPersonId(),
//                person.getFirstName(),
//                person.getMiddleName(),
//                person.getLastName(),
//                person.getNepaliFirstName(),
//                person.getNepaliMiddleName(),
//                person.getNepaliLastName(),
//                person.getDateOfBirth(),
//                person.getGender(),
//                person.getBloodGroup(),
//                person.getMartialStatus(),
//                person.getNationality(),
//                person.getProfilePhoto(),
//                person.getFatherName(),
//                person.getNepaliFatherName(),
//                person.getMotherName(),
//                person.getNepaliMotherName(),
//                person.getProvince(),
//                person.getDistrict(),
//                person.getMunicipality(),
//                person.getWardNo(),
//                person.getTemporaryProvince(),
//                person.getTemporaryDistrict(),
//                person.getTemporaryMunicipality(),
//                person.getTemporaryWardNo(),
//                person.isStatus()
//        );
//    }

    @Override
    public PersonInfoResponse getUserInfo(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getClaimAsString("userId");

        // Fetch the Person entity using the userId
        Person person = personRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Person not found for userId: " + userId));

        // Map the Person entity to PersonInfoResponse
        return new PersonInfoResponse(person);
    }
}