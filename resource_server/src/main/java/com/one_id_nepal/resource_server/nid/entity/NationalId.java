package com.one_id_nepal.resource_server.nid.entity;

import com.one_id_nepal.resource_server.person.entity.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NationalId {
    @Id
    private String nID;

    @Column(nullable = false, unique = true)
    private String nidNumber;

    @Column(nullable = false, unique = true)
    private String fatherNidNumber;

    @Column(nullable = false, unique = true)
    private String fatherCitizenshipNumber;

    @Column(nullable = false, unique = true)
    private String motherNidNumber;

    @Column(nullable = false, unique = true)
    private String motherCitizenshipNumber;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "personId", nullable = false)
    private Person person;

}
