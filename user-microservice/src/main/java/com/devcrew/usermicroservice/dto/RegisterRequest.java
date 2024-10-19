package com.devcrew.usermicroservice.dto;

import com.devcrew.usermicroservice.model.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String user_name;
    private String mail;
    private String password;
    private boolean authenticated;
    private LocalDate user_created_at;
    private LocalDate user_updated_at;
    private Role role;
    @ToString.Exclude
    private PersonDTO person;
}
