package com.oneidnepal.auth_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String phoneNumber;
    private String password;
    private String scope;
}

