package com.devcrew.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String user_name;

    // Mail auth not supported yet, just with username
    private String mail;


    private String password;
}
