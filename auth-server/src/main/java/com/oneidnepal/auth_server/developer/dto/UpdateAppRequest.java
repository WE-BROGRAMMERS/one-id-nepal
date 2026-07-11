package com.oneidnepal.auth_server.developer.dto;


import lombok.Data;

import java.util.List;

/**
 * @author Utsab Dahal
 */
@Data
public class UpdateAppRequest {

    private String appName;

    private List<String> redirectUris;

    private List<String> postLogoutRedirectUris;

    private List<String> scopes;

    private boolean active;
}