package com.devcrew.usermicroservice.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String user_name;
    private String mail;
    private String password;
    private boolean authenticated;
    private LocalDate user_created_at;
    private LocalDate user_updated_at;
    @JsonManagedReference
    @ToString.Exclude
    private PersonDTO person;
}
