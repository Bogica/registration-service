package com.wevolv.registrationservice.keycloak.service.impl;

import com.wevolv.registrationservice.communication.service.CommunicationService;
import com.wevolv.registrationservice.user.model.dto.RegistrationRequestDto;
import com.wevolv.registrationservice.keycloak.service.KeycloakService;
import com.wevolv.registrationservice.user.model.GenericApiResponse;
import com.wevolv.registrationservice.user.model.User;
import com.wevolv.registrationservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final UserService userService;
    private final CommunicationService communicationService;

    final ModelMapper modelMapper = new ModelMapper();

    @Value("${keycloak.credentials.secret}")
    private String SECRET_KEY;

    @Value("${keycloak.resource}")
    private String CLIENT_ID;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${keycloak.auth-server-url}")
    private String AUTH_URL;

    @Value("${admin.username}")
    private String ADMIN_USERNAME;

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;


    @Override
    public String getKeycloak() {
        return "keycloak radi";
    }

    @Override
    public void createUserInKeycloak(RegistrationRequestDto registrationRequestDto) {
        try {
            UsersResource usersResource = getKeycloakUserResource();
            UserRepresentation user = new UserRepresentation();

            user.setEnabled(true);
            user.setEmailVerified(false);
            user.setUsername(registrationRequestDto.getEmail());
            user.setFirstName(registrationRequestDto.getFirstName());
            user.setLastName(registrationRequestDto.getLastName());
            user.setEmail(registrationRequestDto.getEmail());

            // check if user already exists in keycloak database
            List<UserRepresentation> search = getRealmResource().users().search(user.getUsername());

            //check if user exists in user-service database
            Optional<User> userExists = userService.findUserByEmail(registrationRequestDto.getEmail());
            userExists.ifPresent(u -> { throw new IllegalArgumentException(String.format("User with email %s already exists", registrationRequestDto.getEmail()));});

            if(search.size() >= 1){
                throw new IllegalArgumentException(String.format("User with username %s already exists!", user.getUsername()));
            } else {

                User userApiResponse = userService.addUserMongo(registrationRequestDto);
//                GenericApiResponse userApi = userApiResponse.getResponse();
//                assert userApi != null;
//                User userMapped = modelMapper.map(userApi.getResponse(), User.class);

                String userId = userApiResponse.getId();

                Response response = usersResource.create(user);
                if ((response.getStatus() == 201)) {
                    String keycloakId = CreatedResponseUtil.getCreatedId(response);
                    log.info("Created user with keycloakId {} and username {}", keycloakId, user.getUsername());

                    // create password credential
                    CredentialRepresentation passwordCred = new CredentialRepresentation();
                    passwordCred.setTemporary(false);
                    passwordCred.setType(CredentialRepresentation.PASSWORD);
                    passwordCred.setValue(registrationRequestDto.getPassword());

                    //TODO call Twilio Service and send email for verification

                    String communicationApiResponse = communicationService.sendMailActivateAccount(registrationRequestDto.getEmail(), userId);
                    System.out.println("");

                    //TODO Djole za Teu napisi koje endopinte da gadam

                } else {
                    log.info("Username = " + user.getEmail() + " could not be created in keycloak");
                    throw new IllegalArgumentException("Username = " + user.getEmail() + " could not be created in Keycloak");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private UsersResource getKeycloakUserResource() {

        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(AUTH_URL)
                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
                .username(ADMIN_USERNAME).password(ADMIN_PASSWORD)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();

        RealmResource realmResource = keycloak.realm(REALM);
        return realmResource.users();
    }

    private RealmResource getRealmResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTH_URL).realm("master").username(ADMIN_USERNAME).password(ADMIN_PASSWORD)
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        return kc.realm(REALM);

    }
}



