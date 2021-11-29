package com.wevolv.registrationservice.communication.service;

import com.wevolv.registrationservice.user.model.GenericApiResponse;
import org.springframework.http.ResponseEntity;

public interface CommunicationService {

    public String test();

    String sendMailActivateAccount(String email, String userId);
}
