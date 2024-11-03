package com.devcrew.usermicroservice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * PersonDTO is a class used to create a data transfer object for the person entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
        /**
         * The id field is used to store the identifier of the person.
         */
        private Integer id;
        /**
         * The user_real_name field is used to store the real name of the person.
         */
        private String user_real_name;
        /**
         * The user_last_name field is used to store the last name of the person.
         */
        private String user_last_name;
        /**
         * The user_date_of_birth field is used to store the date of birth of the person.
         */
        private LocalDate user_date_of_birth;
        /**
         * The user_personal_Info field is used to store the personal information of the person.
         */
        private String user_personal_Info;
        /**
         * The user_age field is used to store the age of the person.
         */
        private Integer user_age;
        /**
         * The user field is used to store the user object of the person. (User related to the person)
         */
        @JsonBackReference
        @ToString.Exclude
        private UserDTO user;
}
