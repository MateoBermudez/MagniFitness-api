package com.devcrew.usermicroservice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
        private Integer id;
        private String user_real_name;
        private String user_last_name;
        private LocalDate user_date_of_birth;
        private String user_personal_Info;
        private Integer user_age;
        @JsonBackReference
        @ToString.Exclude
        private UserDTO user;
}
