package com.one_id_nepal.resource_server.api.dto.response;

import com.one_id_nepal.resource_server.nid.entity.NationalId;
import com.one_id_nepal.resource_server.person.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NidResponse {
    private String nID;
    private String nidNumber;
    private String fullName;
    private String nepaliFullName;
    private String dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String fatherName;
    private String nepaliFatherName;
    private String fatherCitizenshipNumber;
    private String motherName;
    private String nepaliMotherName;
    private String motherCitizenshipNumber;

    public NidResponse(Person person, NationalId nationalId) {
        this.nID = nationalId.getNID();
        this.nidNumber = nationalId.getNidNumber();
        this.fullName = person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName();
        this.nepaliFullName = person.getNepaliFirstName() + " " + person.getNepaliMiddleName() + " " + person.getNepaliLastName();
        this.dateOfBirth = person.getDateOfBirth();
        this.gender = person.getGender();
        this.maritalStatus = person.getMartialStatus();
        this.fatherName = person.getFatherName();
        this.nepaliFatherName = person.getNepaliFatherName();
        this.fatherCitizenshipNumber = nationalId.getFatherCitizenshipNumber();
        this.motherName = person.getMotherName();
        this.nepaliMotherName = person.getNepaliMotherName();
        this.motherCitizenshipNumber = nationalId.getMotherCitizenshipNumber();

    }
}
