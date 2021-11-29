package com.wevolv.registrationservice.google.config;

import com.wevolv.registrationservice.google.service.GoogleService;
import com.wevolv.registrationservice.google.service.impl.GoogleServiceImpl;
import com.wevolv.registrationservice.keycloak.service.KeycloakService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GoogleIntegrationConfiguration {

    @Bean
    RestTemplate getGoogleRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public GoogleService getGoogleService(RestTemplate getGoogleRestTemplate, KeycloakService keycloakService){
        return new GoogleServiceImpl(getGoogleRestTemplate, keycloakService);
    }

}
