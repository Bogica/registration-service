package com.wevolv.registrationservice.google.service.impl;

import com.wevolv.registrationservice.google.model.AuthCode;
import com.wevolv.registrationservice.google.model.dto.GoogleTokenDto;
import com.wevolv.registrationservice.google.service.GoogleService;
import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;
import com.wevolv.registrationservice.keycloak.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Slf4j

public class GoogleServiceImpl implements GoogleService {

    private final RestTemplate restTemplate;
    private final KeycloakService keycloakService;

    public GoogleServiceImpl(RestTemplate restTemplate, KeycloakService keycloakService) {
        this.restTemplate = restTemplate;
        this.keycloakService = keycloakService;
    }

    @Value("${gmail.credentials.secret}")
    private String SECRET_KEY;

    @Value("${gmail.scope}")
    private String SCOPE;

    @Value("${gmail.clientid}")
    private String CLIENT_ID;

    @Value("${gmail.auth-server-url}")
    private String AUTH_URL;

    private String REDIRECT_URI = "http://localhost:8080/auth/realms/registerapirealm/broker/google/endpoint";

    @Override
    public String getGoogle() {
        return "Radi google";
    }

    @Override
    public RegistrationRequestDto registerUser(AuthCode authCode) {

        GoogleTokenDto googleToken = requestGoogleAccessAuth(authCode);
        return getUserProfile(googleToken);
    }

    private GoogleTokenDto requestGoogleAccessAuth(AuthCode authCode) {
        GoogleTokenDto accessInfo = null;
        try {
            MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<>();
            urlParameters.add("grant_type", "authorization_code");
            urlParameters.add("client_id", CLIENT_ID);
            urlParameters.add("response_type", "code");
            urlParameters.add("scope", SCOPE);
            urlParameters.add("redirect_uri", REDIRECT_URI);
            urlParameters.add("client_secret", SECRET_KEY);
            urlParameters.add("access_type", "offline");
            urlParameters.add("code", authCode.getAuthCode());

            accessInfo = authenticate(urlParameters);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessInfo;
    }

    private RegistrationRequestDto getUserProfile(GoogleTokenDto googleToken) {
        String uri = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + googleToken.getAccess_token();


        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            // httpHeaders.set("Authorization", "Bearer " + responseToken.getAccess_token());
            HttpEntity<String> request = new HttpEntity<String>(httpHeaders);

            ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.POST, request, Object.class);
            log.info("{}", result);
            log.info("{}", result.getBody());

            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) result.getBody();


            if (map != null) {
              /*  userProfile.setUserId(map.get("sub").toString());
                userProfile.setGiven_name(map.get("given_name").toString());
                userProfile.setFamily_name(map.get("family_name").toString());
                userProfile.setEmail(map.get("email").toString());
                userProfile.setEmail_verified(map.get("email_verified").toString());*/
                //userProfile.setPhoto(Optional.ofNullable(map.get("photo").toString()));
            }
            //TODO: call user service and see if user exists, if it doesn't save it to database user and keycloak

            keycloakService.createUserInKeycloak(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private GoogleTokenDto authenticate(MultiValueMap<String, String> urlParameters) {
        GoogleTokenDto accessTokenInfo = new GoogleTokenDto();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(urlParameters, httpHeaders);

            ResponseEntity<Object> result = restTemplate.exchange(AUTH_URL, HttpMethod.POST, request, Object.class);
            log.info("{}", result);
            log.info("{}", result.getBody());

            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) result.getBody();

            if (map != null) {

                accessTokenInfo.setAccess_token(map.get("access_token").toString());
                accessTokenInfo.setExpires_in(map.get("expires_in").toString());
                accessTokenInfo.setToken_type(map.get("token_type").toString());
                accessTokenInfo.setScope(map.get("scope").toString());
                accessTokenInfo.setId_token(map.get("id_token").toString());
                //accessTokenInfo.setRefresh_token(Optional.of(map.get("refresh_token").toString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessTokenInfo;
    }
}
