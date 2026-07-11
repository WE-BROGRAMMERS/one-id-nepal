package com.oneidnepal.auth_server.developer.dto;

import com.oneidnepal.auth_server.developer.entity.ClientType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * @author Utsab Dahal
 */
@Data
@Builder
public class AppResponse {

    private String id;

    private String appName;

    private String clientId;

    private String clientSecret;

    private ClientType clientType;

    private boolean active;

    private Instant createdAt;
}