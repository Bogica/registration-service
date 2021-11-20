package com.wevolv.registrationservice;

import config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.TestService;

@SpringBootApplication
public class RegistrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationServiceApplication.class, args);

		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

		TestService obj = (TestService) context.getBean("demoService");

		System.out.println( obj.getServiceName() );
	}
}
