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
import java.util.List;
import java.util.Map;
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
    public Object getUserData(Jwt jwt) throws AccessDeniedException {
        String userId = jwt.getClaimAsString("userId");
        String scope = jwt.getClaimAsString("scope");

        // Fetch the Person entity
        Person person = personRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found for userId: " + userId));

        // Check if the scope includes citizenship_data
        if (scope != null && scope.contains("openid profile citizenship_data")) {
            Citizenship citizenship = person.getCitizenship();
            if (citizenship == null) {
                throw new EntityNotFoundException("Citizenship not found for personId: " + person.getPersonId());
            }
            return new CitizenshipResponse(citizenship, person);
        } else if (scope != null && scope.contains("nid_data")) {
            NationalId nationalId = person.getNid();
            if (nationalId == null) {
                throw new EntityNotFoundException("National ID not found for personId: " + person.getPersonId());
            }
            return new NidResponse(person, nationalId);
        } else if (scope != null && scope.contains("citizenship_nid")) {
            Citizenship citizenship = person.getCitizenship();
            if (citizenship == null) {
                throw new EntityNotFoundException("Citizenship not found for personId: " + person.getPersonId());
            }
            CitizenshipResponse citizenshipResponse = new CitizenshipResponse(citizenship, person);

            NationalId nationalId = person.getNid();
            if (nationalId == null) {
                throw new EntityNotFoundException("National ID not found for personId: " + person.getPersonId());
            }
            NidResponse nidResponse = new NidResponse(person, nationalId);

            return Map.of(
                    "data", List.of(
                            Map.of("citizenship", citizenshipResponse),
                            Map.of("nid", nidResponse)
                    )
            );
        } else {
            throw new AccessDeniedException("Insufficient scope to access data");
        }
    }
}
