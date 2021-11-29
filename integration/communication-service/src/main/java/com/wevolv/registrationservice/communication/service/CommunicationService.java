package com.wevolv.registrationservice.communication.service;

import com.wevolv.registrationservice.user.model.GenericApiResponse;
import org.springframework.http.ResponseEntity;

public interface CommunicationService {

    public String test();

    ResponseEntity<GenericApiResponse> sendMailActivateAccount(String email, String userId);
}
