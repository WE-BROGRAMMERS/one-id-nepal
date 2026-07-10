package com.one_id_nepal.resource_server.person.repository;

import com.one_id_nepal.resource_server.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
}
