package com.wevolv.registrationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TestService;

@RestController
public class Controller {

    @Value("${test.name}")
     private String name;

    @GetMapping("/test")
    public String test(){
        return name;
    }

}
