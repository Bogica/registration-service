package com.wevolv.registrationservice.google.model.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class GoogleTokenDto {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String id_token;
    private Optional<String> refresh_token;
    private String scope;
}
