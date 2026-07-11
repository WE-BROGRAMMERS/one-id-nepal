package com.oneidnepal.auth_server.developer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevLoginRequest {
    private String phoneNumber;
    private String password;
    private String scope;
}
