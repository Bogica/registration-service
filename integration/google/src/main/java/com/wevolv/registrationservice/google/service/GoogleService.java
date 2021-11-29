package com.wevolv.registrationservice.google.service;

import com.wevolv.registrationservice.google.model.AuthCode;
import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;

public interface GoogleService {

    String getGoogle();

    RegistrationRequestDto registerUser(AuthCode authCode);
}
