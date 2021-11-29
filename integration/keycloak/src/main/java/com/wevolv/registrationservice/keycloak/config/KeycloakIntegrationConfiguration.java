package com.wevolv.registrationservice.keycloak.config;

import com.wevolv.registrationservice.communication.service.CommunicationService;
import com.wevolv.registrationservice.keycloak.service.KeycloakService;
import com.wevolv.registrationservice.keycloak.service.impl.KeycloakServiceImpl;
import com.wevolv.registrationservice.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KeycloakIntegrationConfiguration {

    @Bean
    KeycloakService getKeycloakService(UserService getUserService, CommunicationService communicationService){
        return new KeycloakServiceImpl(getUserService, communicationService);
    }
}
