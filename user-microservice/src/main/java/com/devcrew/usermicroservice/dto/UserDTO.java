package com.devcrew.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String user_name;
    private String mail;

    //Hash the password before storing it in the database
    private String password;
}
