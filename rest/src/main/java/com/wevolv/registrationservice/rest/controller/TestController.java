package com.wevolv.registrationservice.rest.controller;

import com.wevolv.registrationservice.communication.service.CommunicationService;
import com.wevolv.registrationservice.google.service.GoogleService;
import com.wevolv.registrationservice.keycloak.service.KeycloakService;
import com.wevolv.registrationservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final CommunicationService communicationService;
    private final GoogleService googleService;
    private final KeycloakService keycloakService;
    private final UserService userService;

    @Value("${test.name}")
    private String name;

    @GetMapping("/test")
    public String test(){
        return name;
    }

    @GetMapping("/test2")
    public String test2(){
        return communicationService.test();
    }

    @GetMapping("/test3")
    public String test3(){ return googleService.getGoogle();}

    @GetMapping("/test4")
    public String test4(){ return keycloakService.getKeycloak();}

    @GetMapping("/test5")
    public String test5(){ return userService.getUser();}
}
