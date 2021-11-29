package com.wevolv.registrationservice.communication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wevolv.registrationservice.communication.service.CommunicationService;
import com.wevolv.registrationservice.user.model.GenericApiResponse;
import com.wevolv.registrationservice.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String test() {
        return "User service radi";
    }

    @Override
    public ResponseEntity<GenericApiResponse> sendMailActivateAccount(String email, String userId) {
        ObjectNode root = objectMapper.createObjectNode();
            root.put("email", email);
            root.put("userId", userId);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity = new HttpEntity<>(root, httpHeaders);

        try{
            User userSaved = restTemplate.exchange(
                    "http://localhost:8084/sendMailActivateAccount",
                    HttpMethod.POST,
                    requestEntity,
                    User.class
            ).getBody();
            System.out.println(userSaved);
            return((userSaved == null) ? genericApiResponseFailed(userSaved) : genericApiResponseSuccess(userSaved));
        }catch (HttpStatusCodeException e){
            if(e.getStatusCode().is4xxClientError()) {
                throw HttpClientErrorException.create(
                        e.getStatusCode(),
                        e.getStatusText(),
                        Optional.ofNullable(e.getResponseHeaders()).orElseGet(HttpHeaders::new),
                        e.getResponseBodyAsString().getBytes(),
                        null);
            } else if(e.getStatusCode().is5xxServerError()){
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
                .message("Email is sent.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private ResponseEntity<GenericApiResponse> genericApiResponseFailed(User user) {
        GenericApiResponse apiResponse = GenericApiResponse.builder()
                .error("Not able to send email")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

}
