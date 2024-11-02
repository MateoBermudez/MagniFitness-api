package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;

/**
 * PersonMapper is a class that maps AppPerson objects to PersonDTO objects and vice versa.
 */
public class PersonMapper {

    /**
     * Maps an AppPerson object to a PersonDTO object.
     *
     * @param appPerson the AppPerson object to be mapped
     * @return the PersonDTO object
     */
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

    /**
     * Maps a PersonDTO object to an AppPerson object.
     * If the id is null, it will be ignored.
     *
     * @param personDTO the PersonDTO object to be mapped
     * @return the AppPerson object
     */
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
                personDTO.getUser().isAuthenticated(),
                personDTO.getUser().getUser_created_at(),
                personDTO.getUser().getUser_updated_at(),
                person,
                personDTO.getUser().getRole(),
                personDTO.getUser().getImage()
        );

        if (personDTO.getUser().getId() != null) {
            user.setId(personDTO.getUser().getId());
        }

        person.setAppUser(user);

        return person;
    }

}
