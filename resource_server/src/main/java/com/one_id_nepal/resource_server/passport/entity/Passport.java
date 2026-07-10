package com.one_id_nepal.resource_server.passport.entity;

import com.one_id_nepal.resource_server.person.entity.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String passportId;

    @Column(nullable = false, unique = true)
    private String passportNumber;

    private String issuedDate;
    private String expiryDate;
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "personId", nullable = false)
    private Person person;
}
