package com.wevolv.registrationservice.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.springframework.util.Assert.notNull;

@NoArgsConstructor
@Builder
@Data
public class RegistrationRequestDto {

    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    public RegistrationRequestDto(
            String email,
            String username,
            String password,
            String confirmPassword,
            String firstName,
            String lastName) {
        notNull(email, "email must be set");
        notNull(username, "username must be set");
        notNull(password, "password must be set");
        notNull(confirmPassword, "confirmPassword must be set");
        notNull(firstName, "firstName must be set");
        notNull(lastName, "lastName must be set");
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
