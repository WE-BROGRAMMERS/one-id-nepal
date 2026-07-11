package com.oneidnepal.auth_server.developer.repository;

import com.oneidnepal.auth_server.developer.entity.DeveloperApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Utsab Dahal
 */
@Repository
public interface DeveloperAppRepository extends JpaRepository<DeveloperApp, String> {

    List<DeveloperApp> findByOwnerUserId(String ownerUserId);

    Optional<DeveloperApp> findByClientId(String clientId);

    Optional<DeveloperApp> findByRegisteredClientId(String registeredClientId);
}