package com.one_id_nepal.resource_server.driving_licence.repository;

import com.one_id_nepal.resource_server.driving_licence.entity.DrivingLicence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrivingLicenceRepository extends JpaRepository<DrivingLicence, String> {
}
