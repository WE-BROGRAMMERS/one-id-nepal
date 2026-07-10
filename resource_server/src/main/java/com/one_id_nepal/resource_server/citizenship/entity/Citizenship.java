package com.one_id_nepal.resource_server.citizenship.entity;

import com.one_id_nepal.resource_server.person.entity.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Citizenship {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String citizenshipId;
    @Column(nullable = false, unique = true)
    private String citizenshipNumber;
    private String issuedDate;
    private String issuedDistrict;
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "personId", nullable = false)
    private Person person;
}
