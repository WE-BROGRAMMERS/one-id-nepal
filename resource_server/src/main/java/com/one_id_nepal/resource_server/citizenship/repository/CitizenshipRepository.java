package com.one_id_nepal.resource_server.citizenship.repository;

import com.one_id_nepal.resource_server.citizenship.entity.Citizenship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenshipRepository extends JpaRepository<Citizenship, String> {
}
