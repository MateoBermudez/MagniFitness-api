package com.devcrew.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
        private String user_name;
        private String user_last_name;
        private LocalDate user_date_of_birth;
        private String user_personal_Info;
        private Integer user_age;
}
