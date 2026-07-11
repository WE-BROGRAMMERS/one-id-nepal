package com.oneidnepal.auth_server.developer.controller;


import com.oneidnepal.auth_server.developer.dto.AppResponse;
import com.oneidnepal.auth_server.developer.dto.CreateAppRequest;
import com.oneidnepal.auth_server.developer.dto.UpdateAppRequest;
import com.oneidnepal.auth_server.developer.service.DeveloperAppService;
import com.oneidnepal.auth_server.dto.*;
import com.oneidnepal.auth_server.entity.User;
import com.oneidnepal.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/developer/apps")
@RequiredArgsConstructor
public class DeveloperAppController {

    private final DeveloperAppService developerAppService;
    private final UserRepository userRepository;

    @PostMapping
    public AppResponse create(@RequestBody CreateAppRequest req, Authentication auth) {
        return developerAppService.createApp(currentUserId(auth), req);
    }

    @GetMapping
    public List<AppResponse> list(Authentication auth) {
        return developerAppService.getMyApps(currentUserId(auth));
    }

    @GetMapping("/{appId}")
    public AppResponse get(@PathVariable String appId, Authentication auth) {
        return developerAppService.getApp(currentUserId(auth), appId);
    }

    @PutMapping("/{appId}")
    public AppResponse update(@PathVariable String appId, @RequestBody UpdateAppRequest req, Authentication auth) {
        return developerAppService.updateApp(currentUserId(auth), appId, req);
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> delete(@PathVariable String appId, Authentication auth) {
        developerAppService.deleteApp(currentUserId(auth), appId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{appId}/rotate-secret")
    public AppResponse rotateSecret(@PathVariable String appId, Authentication auth) {
        return developerAppService.rotateClientSecret(currentUserId(auth), appId);
    }

    private String currentUserId(Authentication auth) {
        return userRepository.findByPhoneNumber(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user"));
    }
}