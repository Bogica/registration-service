package com.wevolv.registrationservice.user.config;

import com.wevolv.registrationservice.user.service.UserService;
import com.wevolv.registrationservice.user.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserServiceIntegrationConfiguration {

    @Bean
    RestTemplate getUserServiceRestTemplate(){ return new RestTemplate();}

    @Bean
    UserService getUserService(RestTemplate getUserServiceRestTemplate){
        return new UserServiceImpl(getUserServiceRestTemplate);
    }
}
