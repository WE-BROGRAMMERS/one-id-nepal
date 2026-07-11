package com.one_id_nepal.resource_server.api.dto.response;

import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import com.one_id_nepal.resource_server.person.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenshipResponse {
    private String citizenshipId;
    private String citizenshipNumber;
    private URI profilePhoto;
   private String fullName;
   private String nepaliFullName;
    private String dateOfBirth;
    private String fatherName;
    private String nepaliFatherName;
    private String motherName;
    private String nepaliMotherName;
    private String permanentAddress;
    private String temporaryAddress;
    private String issuedDate;
    private String issuedDistrict;


    public CitizenshipResponse(Citizenship citizenship, Person person) {
        this.citizenshipId = citizenship.getCitizenshipId();
        this.citizenshipNumber = citizenship.getCitizenshipNumber();
        this.profilePhoto = URI.create(person.getProfilePhoto());
        this.fullName = person.getFirstName() + " " + (person.getMiddleName() != null ? person.getMiddleName() + " " : "") + person.getLastName();
        this.nepaliFullName = person.getNepaliFirstName() + " " + (person.getNepaliMiddleName() != null ? person.getNepaliMiddleName() + " " : "") + person.getNepaliLastName();
        this.dateOfBirth = person.getDateOfBirth();
        this.fatherName = person.getFatherName();
        this.nepaliFatherName = person.getNepaliFatherName();
        this.motherName = person.getMotherName();
        this.nepaliMotherName = person.getNepaliMotherName();
        this.permanentAddress = person.getMunicipality() + "-" + person.getWardNo() + ", " + person.getDistrict();
        this.temporaryAddress = person.getTemporaryMunicipality() + "-" + person.getTemporaryWardNo() + ", " + person.getTemporaryDistrict();
        this.issuedDate = citizenship.getIssuedDate();
        this.issuedDistrict = citizenship.getIssuedDistrict();
    }
}
