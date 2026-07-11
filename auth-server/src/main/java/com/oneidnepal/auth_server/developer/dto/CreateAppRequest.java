package com.oneidnepal.auth_server.developer.dto;

import com.oneidnepal.auth_server.developer.entity.ClientType;
import lombok.Data;

import java.util.List;


/**
 * @author Utsab Dahal
 */
@Data
public class CreateAppRequest {

    private String appName;

    private ClientType clientType;

    private List<String> redirectUris;

    private List<String> postLogoutRedirectUris;

    private List<String> scopes;
}