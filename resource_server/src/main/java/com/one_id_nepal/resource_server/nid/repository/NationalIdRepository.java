package com.one_id_nepal.resource_server.nid.repository;

import com.one_id_nepal.resource_server.nid.entity.NationalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationalIdRepository extends JpaRepository<NationalId, String> {
}
