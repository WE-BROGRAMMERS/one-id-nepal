package com.one_id_nepal.resource_server.passport.repository;

import com.one_id_nepal.resource_server.passport.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, String> {
}
