package com.wevolv.registrationservice.rest;

import com.wevolv.registrationservice.communication.config.CommunicationServiceIntegrationConfiguration;
import com.wevolv.registrationservice.google.config.GoogleIntegrationConfiguration;
import com.wevolv.registrationservice.keycloak.config.KeycloakIntegrationConfiguration;
import com.wevolv.registrationservice.user.config.UserServiceIntegrationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import({
        CommunicationServiceIntegrationConfiguration.class,
        GoogleIntegrationConfiguration.class,
        KeycloakIntegrationConfiguration.class,
        UserServiceIntegrationConfiguration.class
})
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
