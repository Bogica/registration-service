package com.wevolv.registrationservice.user.service.impl;

import com.wevolv.registrationservice.user.model.GenericApiResponse;
import com.wevolv.registrationservice.user.model.User;
import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;
import com.wevolv.registrationservice.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    @Override
    public Optional<User> findUserByEmail(String email) {

        try {
            User user = restTemplate.exchange(
                    "http://localhost:8086/user/{email}",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    User.class,
                    email
            ).getBody();
            System.out.println(user);
            return ((user == null) ? Optional.empty() : Optional.of(user));
        } catch (HttpStatusCodeException e) {
            log.error("Error getting user data for user with email: {}  with statusCode: {}", email, e.getStatusCode());
            String formattedErrorMessage = String.format("Error getting user data for user with email: %s. With exceptionMessage: %s", email, e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            }
            if (e.getStatusCode().is4xxClientError()) {
                throw HttpClientErrorException.create(formattedErrorMessage,
                        e.getStatusCode(),
                        e.getStatusText(),
                        Optional.ofNullable(e.getResponseHeaders()).orElseGet(HttpHeaders::new),
                        e.getResponseBodyAsString().getBytes(),
                        null);
            } else if (e.getStatusCode().is5xxServerError()) {
                throw HttpServerErrorException.create(formattedErrorMessage,
                        e.getStatusCode(),
                        e.getStatusText(),
                        Optional.ofNullable(e.getResponseHeaders()).orElseGet(HttpHeaders::new),
                        e.getResponseBodyAsString().getBytes(),
                        null);
            } else {
                throw e;
            }
        }
    }

    @Override
    public User addUserMongo(RegistrationRequestDto registrationRequestDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(registrationRequestDto, httpHeaders);
        try {
            GenericApiResponse userSaved = restTemplate.exchange(
                    "http://localhost:8086/user/",
                    HttpMethod.POST,
                    requestEntity,
                    GenericApiResponse.class
            ).getBody();

            User user = new User();
            if (userSaved != null) {
                LinkedHashMap<String,String> map = (LinkedHashMap<String, String>) userSaved.getResponse();
                user.setId(map.get("id"));
                user.setEmail(map.get("email"));
                user.setUsername(map.get("username"));
                user.setEmailVerified(false);
                user.setKeycloakId(map.get("keycloakId"));
                user.setFirstName(map.get("firstName"));
                user.setLastName(map.get("lastName"));
            }


            System.out.println(userSaved);
            return ((userSaved == null) ? null : user);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw HttpClientErrorException.create(
                        e.getStatusCode(),
                        e.getStatusText(),
                        Optional.ofNullable(e.getResponseHeaders()).orElseGet(HttpHeaders::new),
                        e.getResponseBodyAsString().getBytes(),
                        null);
            } else if (e.getStatusCode().is5xxServerError()) {
                throw HttpServerErrorException.create(
                        e.getStatusCode(),
                        e.getStatusText(),
                        Optional.ofNullable(e.getResponseHeaders()).orElseGet(HttpHeaders::new),
                        e.getResponseBodyAsString().getBytes(),
                        null);
            } else {
                throw e;
            }
        }
    }

    private ResponseEntity<GenericApiResponse> genericApiResponseSuccess(User user) {

        GenericApiResponse apiResponse = GenericApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .response(user)
                .message("User is added to mongo database.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private ResponseEntity<GenericApiResponse> genericApiResponseFailed() {
        GenericApiResponse apiResponse = GenericApiResponse.builder()
                .error("Not able to add user")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @Override
    public String getUser() {
        return "user service radi";
    }
}
