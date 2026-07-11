package com.one_id_nepal.resource_server.person.entity;

import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import com.one_id_nepal.resource_server.nid.entity.NationalId;
import jakarta.persistence.*;
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
    private String userId;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Citizenship citizenship;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private NationalId nid;

}
