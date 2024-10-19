package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import org.springframework.security.core.userdetails.User;

public class PersonMapper {

    public static PersonDTO toDTO(AppPerson appPerson) {
        if (appPerson == null) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(appPerson.getId());
        personDTO.setUser_real_name(appPerson.getName());
        personDTO.setUser_last_name(appPerson.getLast_name());
        personDTO.setUser_date_of_birth(appPerson.getDate_of_birth());
        personDTO.setUser_personal_Info(appPerson.getPersonalInfo());
        personDTO.setUser_age(appPerson.getAge());

        return personDTO;
    }

    public static AppPerson toEntity(PersonDTO personDTO) {
        if (personDTO == null) {
            return null;
        }



        AppPerson person = new AppPerson(
                personDTO.getUser_real_name(),
                personDTO.getUser_last_name(),
                personDTO.getUser_date_of_birth(),
                personDTO.getUser_personal_Info(),
                personDTO.getUser_age(),
                null
        );

        if (personDTO.getId() != null) {
            person.setId(personDTO.getId());
        }

        if (personDTO.getUser() == null) {
            return person;
        }

        AppUser user = new AppUser(
                personDTO.getUser().getUser_name(),
                personDTO.getUser().getMail(),
                personDTO.getUser().getPassword(),
                personDTO.getUser().isAuthenticated(),
                personDTO.getUser().getUser_created_at(),
                personDTO.getUser().getUser_updated_at(),
                person,
                personDTO.getUser().getRole()
        );

        if (personDTO.getUser().getId() != null) {
            user.setId(personDTO.getUser().getId());
        }

        person.setAppUser(user);

        return person;
    }

}
