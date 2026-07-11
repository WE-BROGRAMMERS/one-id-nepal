package com.one_id_nepal.resource_server.config.init_db;

import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import com.one_id_nepal.resource_server.nid.entity.NationalId;
import com.one_id_nepal.resource_server.person.entity.Person;
import com.one_id_nepal.resource_server.person.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialDataConfig {

    private final PersonRepository personRepository;

    @PostConstruct
    public void initData() {
        List<String> userIds = Arrays.asList(
                "1e7f3c4a-9b5a-4c2f-8123-111111111111",
                "2a8d4b5c-6c7d-4e8f-9123-222222222222",
                "3b9e5c6d-7d8e-4f9a-a123-333333333333",
                "4c0f6d7e-8e9f-40ab-b123-444444444444",
                "5d1a7e8f-9f0a-41bc-c123-555555555555",
                "6e2b8f90-0a1b-42cd-d123-666666666666",
                "7f3c9012-1b2c-43de-e123-777777777777",
                "809da123-2c3d-44ef-f123-888888888888",
                "91aeb234-3d4e-4501-0123-999999999999",
                "a2bfc345-4e5f-4612-1123-aaaaaaaaaaaa",
                "b3c0d456-5f60-4723-2123-bbbbbbbbbbbb",
                "c4d1e567-6071-4834-3123-cccccccccccc",
                "d5e2f678-7182-4945-4123-dddddddddddd",
                "e6f3a789-8293-4a56-5123-eeeeeeeeeeee",
                "f704b89a-93a4-4b67-6123-ffffffffffff",
                "08a5c9ab-a4b5-4c78-7123-111122223333",
                "19b6da9c-b5c6-4d89-8123-222233334444",
                "2ac7ebad-c6d7-4e9a-9123-333344445555",
                "3bd8fcbf-d7e8-40ab-a123-444455556666",
                "4ce9fdc0-e8f9-41bc-b123-555566667777",
                "5df0aec1-f901-42cd-c123-666677778888",
                "6e01bfd2-0a12-43de-d123-777788889999"
        );

        String defaultAvatarUrl = "https://static.vecteezy.com/system/resources/thumbnails/048/216/761/small/modern-male-avatar-with-black-hair-and-hoodie-illustration-free-png.png";

        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);

            // Check if Person with userId already exists
            if (personRepository.findByUserId(userId).isPresent()) {
                continue; // Skip if Person already exists
            }

            Person person = new Person();
            person.setUserId(userId);
            person.setFirstName("DummyFirstName" + i);
            person.setMiddleName("DummyMiddleName" + i);
            person.setLastName("DummyLastName" + i);
            person.setNepaliFirstName("नेपालीपहिलोनाम" + i);
            person.setNepaliMiddleName("नेपालीमध्यनाम" + i);
            person.setNepaliLastName("नेपालीअन्तिमनाम" + i);
            person.setDateOfBirth("2000-01-01");
            person.setGender("Other");
            person.setBloodGroup("O+");
            person.setMartialStatus("Single");
            person.setNationality("Nepali");
            person.setProfilePhoto(defaultAvatarUrl);
            person.setFatherName("DummyFather" + i);
            person.setNepaliFatherName("नेपालीबाबु" + i);
            person.setMotherName("DummyMother" + i);
            person.setNepaliMotherName("नेपालीआमा" + i);
            person.setProvince("Province1");
            person.setDistrict("District1");
            person.setMunicipality("Municipality1");
            person.setWardNo("1");
            person.setTemporaryProvince("Province2");
            person.setTemporaryDistrict("District2");
            person.setTemporaryMunicipality("Municipality2");
            person.setTemporaryWardNo("2");
            person.setStatus(true);

            // Create and associate Citizenship
            Citizenship citizenship = new Citizenship();
            citizenship.setCitizenshipId("CIT-" + userId); // Assign a unique ID
            citizenship.setCitizenshipNumber("CIT-" + userId.substring(0, 8)); // Unique citizenship number
            citizenship.setIssuedDate("2023-01-01");
            citizenship.setIssuedDistrict("District1");
            citizenship.setPerson(person); // Associate with Person

            // Create and associate NationalId
            NationalId nationalId = new NationalId();
            nationalId.setNID("NID-" + userId); // Assign a unique ID
            nationalId.setNidNumber("NID-" + userId.substring(0, 8)); // Unique NID number
            nationalId.setFatherNidNumber("FATHER-NID-" + i); // Ensure uniqueness
            nationalId.setFatherCitizenshipNumber("FATHER-CIT-" + i); // Ensure uniqueness
            nationalId.setMotherNidNumber("MOTHER-NID-" + i); // Ensure uniqueness
            nationalId.setMotherCitizenshipNumber("MOTHER-CIT-" + i); // Ensure uniqueness
            nationalId.setPerson(person); // Associate with Person

            // Set Citizenship and NationalId in Person
            person.setCitizenship(citizenship);
            person.setNid(nationalId);

            // Save Person (and associated entities due to CascadeType.ALL)
            personRepository.save(person);
        }
    }
}