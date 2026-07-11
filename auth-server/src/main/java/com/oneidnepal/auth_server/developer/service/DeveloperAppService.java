package com.oneidnepal.auth_server.developer.service;

import com.oneidnepal.auth_server.developer.dto.AppResponse;
import com.oneidnepal.auth_server.developer.dto.CreateAppRequest;
import com.oneidnepal.auth_server.developer.dto.UpdateAppRequest;

import java.util.List;

/**
 * @author Utsab Dahal
 */
public interface DeveloperAppService {
    AppResponse createApp(String ownerUserId, CreateAppRequest request);

    List<AppResponse> getMyApps(String ownerUserId);

    AppResponse getApp(String ownerUserId, String appId);

    AppResponse updateApp(String ownerUserId, String appId, UpdateAppRequest request);

    void deleteApp(String ownerUserId, String appId);

    AppResponse rotateClientSecret(String ownerUserId, String appId);
}
