package com.one_id_nepal.resource_server.driving_licence.entity;

import com.one_id_nepal.resource_server.person.entity.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DrivingLicence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String drivingLicenceId;

    @Column(nullable = false, unique = true)
    private String drivingLicenceNumber;

    private String licenceType;
    private String category;
    private String issuedDate;
    private String expiryDate;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "personId", nullable = false)
    private Person person;
}
