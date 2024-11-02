package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;

/**
 * UserMapper is a class that maps AppUser objects to UserDTO objects and vice versa.
 */
public class UserMapper {

    /**
     * Maps an AppUser object to a UserDTO object.
     *
     * @param appUser the AppUser object to be mapped
     * @return the UserDTO object
     */
    public static UserDTO toDTO(AppUser appUser) {
        if (appUser == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(appUser.getId());
        userDTO.setUser_name(appUser.getUsername());
        userDTO.setMail(appUser.getEmail());
        userDTO.setAuthenticated(appUser.isAuthenticated());
        userDTO.setUser_created_at(appUser.getCreatedAt());
        userDTO.setUser_updated_at(appUser.getUpdatedAt());
        userDTO.setRole(appUser.getRole());

        return userDTO;
    }


    /**
     * Maps a UserDTO object to an AppUser object.
     * If the id is null, it will be ignored.
     *
     * @param userDTO the UserDTO object to be mapped
     * @return the AppUser object
     */
    public static AppUser toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        AppUser user =  new AppUser(
                userDTO.getUser_name(),
                userDTO.getMail(),
                userDTO.isAuthenticated(),
                userDTO.getUser_created_at(),
                userDTO.getUser_updated_at(),
                null,
                userDTO.getRole(),
                userDTO.getImage()
        );

        if (userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }

        if (userDTO.getPerson() == null) {
            return user;
        }

        AppPerson person = new AppPerson(
                userDTO.getPerson().getUser_real_name(),
                userDTO.getPerson().getUser_last_name(),
                userDTO.getPerson().getUser_date_of_birth(),
                userDTO.getPerson().getUser_personal_Info(),
                userDTO.getPerson().getUser_age(),
                user
        );

        if (userDTO.getPerson().getId() != null) {
            person.setId(userDTO.getPerson().getId());
        }

        user.setAppPerson(person);

        return user;
    }
}
