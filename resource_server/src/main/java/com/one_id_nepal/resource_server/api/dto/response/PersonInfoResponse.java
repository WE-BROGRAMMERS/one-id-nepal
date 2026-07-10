package com.one_id_nepal.resource_server.api.dto.response;

import com.one_id_nepal.resource_server.person.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfoResponse {
    private String personId;

    private String firstName;
    private String middleName;
    private String lastName;
    private String nepaliFirstName;
    private String nepaliMiddleName;
    private String nepaliLastName;
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String martialStatus;
    private String nationality;
    private String profilePhoto;
    private String fatherName;
    private String nepaliFatherName;
    private String motherName;
    private String nepaliMotherName;
    private String province;
    private String district;
    private String municipality;
    private String wardNo;
    private String temporaryProvince;
    private String temporaryDistrict;
    private String temporaryMunicipality;
    private String temporaryWardNo;
    private boolean status;

    public PersonInfoResponse(Person person) {
        this.personId = person.getPersonId();
        this.firstName = person.getFirstName();
        this.middleName = person.getMiddleName();
        this.lastName = person.getLastName();
        this.nepaliFirstName = person.getNepaliFirstName();
        this.nepaliMiddleName = person.getNepaliMiddleName();
        this.nepaliLastName = person.getNepaliLastName();
        this.dateOfBirth = person.getDateOfBirth();
        this.gender = person.getGender();
        this.bloodGroup = person.getBloodGroup();
        this.martialStatus = person.getMartialStatus();
        this.nationality = person.getNationality();
    }
}
