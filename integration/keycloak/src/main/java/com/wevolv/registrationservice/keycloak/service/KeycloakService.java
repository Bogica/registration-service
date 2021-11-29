package com.wevolv.registrationservice.keycloak.service;

import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;

public interface KeycloakService {

    String getKeycloak();
    String createUserInKeycloak(RegistrationRequestDto registrationRequestDto);
}
