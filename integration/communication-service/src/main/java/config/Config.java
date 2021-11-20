package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import service.TestService;
import service.TestServiceImpl;

@Configuration
public class Config {

    @Bean(name = "demoService")
    public TestService getTestService(){
        return new TestServiceImpl();
    }
}
