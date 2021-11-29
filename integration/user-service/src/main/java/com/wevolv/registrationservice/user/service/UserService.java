package com.wevolv.registrationservice.user.service;

import com.wevolv.registrationservice.user.model.GenericApiResponse;
import com.wevolv.registrationservice.user.model.User;
import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String email);
    User addUserMongo(RegistrationRequestDto registrationRequestDto);
    String getUser();
}
