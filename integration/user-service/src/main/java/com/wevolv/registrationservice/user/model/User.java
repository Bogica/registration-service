package com.wevolv.registrationservice.user.model;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String keycloakId;
    private Boolean emailVerified = false;
}
