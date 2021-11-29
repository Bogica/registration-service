package com.wevolv.registrationservice.rest.controller;

import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;
import com.wevolv.registrationservice.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final KeycloakService keycloakService;

    @PostMapping(value = "/register")
    public void createUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
        keycloakService.createUserInKeycloak(registrationRequestDto);
    }
}
