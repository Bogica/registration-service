package com.wevolv.registrationservice.communication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wevolv.registrationservice.communication.service.CommunicationService;
import com.wevolv.registrationservice.communication.service.impl.CommunicationServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommunicationServiceIntegrationConfiguration {

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    @Bean
	public RestTemplate communicationServiceRestTemplate() {return new RestTemplate();}

    @Bean
    public CommunicationService getCommunicationService(RestTemplate communicationServiceRestTemplate, ObjectMapper getObjectMapper){
        return new CommunicationServiceImpl(communicationServiceRestTemplate, getObjectMapper);
    }
}
