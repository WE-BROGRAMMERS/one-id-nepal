package com.one_id_nepal.resource_server.person.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    //relation with resource server with auth server
    private String UUID;
}
