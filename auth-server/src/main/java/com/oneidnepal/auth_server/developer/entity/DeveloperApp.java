package com.oneidnepal.auth_server.developer.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * @author Utsab Dahal
 */
@Entity
@Table(name = "developer_apps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperApp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String registeredClientId;

    @Column(nullable = false)
    private String ownerUserId;

    @Column(nullable = false)
    private String appName;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Builder.Default
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}