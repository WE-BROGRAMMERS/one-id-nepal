package com.one_id_nepal.resource_server.api.dto.response;

import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import com.one_id_nepal.resource_server.nid.entity.NationalId;
import com.one_id_nepal.resource_server.person.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String fullName;
    private String profilePhoto;
    private String address;
    private String dateOfBirth;
    private String bloodType;
    private String gender;
    private String maritalStatus;

    public ProfileResponse(Person person) {
        this.profilePhoto = String.valueOf(URI.create(person.getProfilePhoto()));
        this.fullName = person.getFirstName() + " " + (person.getMiddleName() != null ? person.getMiddleName() + " " : "") + person.getLastName();
        this.dateOfBirth = person.getDateOfBirth();
        this.address = person.getMunicipality() + "-" + person.getWardNo() + ", " + person.getDistrict();
        this.fullName = person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName();
        this.dateOfBirth = person.getDateOfBirth();
        this.gender = person.getGender();
        this.maritalStatus = person.getMartialStatus();
    }
}
