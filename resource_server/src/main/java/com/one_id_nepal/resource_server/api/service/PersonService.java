package com.one_id_nepal.resource_server.api.service;

import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface PersonService {
//    List<PersonInfoResponse> getAllPersons();

    PersonInfoResponse getUserInfo(@AuthenticationPrincipal Jwt jwt);

}
