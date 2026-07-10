package com.one_id_nepal.resource_server.api.service;

import com.one_id_nepal.resource_server.api.dto.response.PersonInfoResponse;

import java.util.List;

public interface PersonService {
    List<PersonInfoResponse> getAllPersons();

}
