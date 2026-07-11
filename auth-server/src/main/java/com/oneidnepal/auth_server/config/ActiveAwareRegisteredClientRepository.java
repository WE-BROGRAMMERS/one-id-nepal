package com.oneidnepal.auth_server.config;


import com.oneidnepal.auth_server.developer.entity.DeveloperApp;
import com.oneidnepal.auth_server.developer.repository.DeveloperAppRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author Utsab Dahal
 */

public class ActiveAwareRegisteredClientRepository implements RegisteredClientRepository {

    private final RegisteredClientRepository delegate;
    private final DeveloperAppRepository developerAppRepository;

    public ActiveAwareRegisteredClientRepository(RegisteredClientRepository delegate,
                                                 DeveloperAppRepository developerAppRepository) {
        this.delegate = delegate;
        this.developerAppRepository = developerAppRepository;
    }

    @Override public void save(RegisteredClient rc) { delegate.save(rc); }

    @Override public RegisteredClient findById(String id) {
        RegisteredClient rc = delegate.findById(id);
        return (rc != null && isActive(rc.getId())) ? rc : null;
    }

    @Override public RegisteredClient findByClientId(String clientId) {
        RegisteredClient rc = delegate.findByClientId(clientId);
        return (rc != null && isActive(rc.getId())) ? rc : null;
    }

    private boolean isActive(String registeredClientId) {
        return developerAppRepository.findByRegisteredClientId(registeredClientId)
                .map(DeveloperApp::isActive)
                .orElse(true);
    }
}