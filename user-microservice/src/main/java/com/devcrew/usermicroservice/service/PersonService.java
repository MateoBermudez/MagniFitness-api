package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.mapper.PersonMapper;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.AuthorizationUtils;
import com.devcrew.usermicroservice.utils.JsonBuilderUtils;
import com.devcrew.usermicroservice.utils.JwtValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing person-related operations.
 * Every method validates the user's permissions before executing the operation.
 */
@Service
public class PersonService {

    /**
     * Person repository, used for accessing person-related data in the database.
     */
    private final PersonRepository personRepository;

    /**
     * User repository, used for accessing user-related data in the database.
     */
    private final UserRepository userRepository;

    /**
     * JWT validation utility class.
     */
    private final JwtValidation jwtValidation;

    /**
     * Role permission repository, used for accessing role permission-related data in the database.
     */
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Log sender service, used for sending logs to the log microservice.
     */
    private final LogSenderService logSenderService;

    /**
     * Constructor for PersonService, uses Dependency Injection to inject the necessary repositories and utilities.
     *
     * @param personRepository the person repository
     * @param userRepository the user repository
     * @param jwtValidation the JWT validation utility
     * @param rolePermissionRepository the role permission repository
     */
    @Autowired
    public PersonService(PersonRepository personRepository, UserRepository userRepository, JwtValidation jwtValidation, RolePermissionRepository rolePermissionRepository, LogSenderService logSenderService) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.jwtValidation = jwtValidation;
        this.rolePermissionRepository = rolePermissionRepository;
        this.logSenderService = logSenderService;
    }

    /**
     * Retrieves a list of all people.
     *
     * @param token the JWT token of the user making the request
     * @return a list of PersonDTO objects with information about each person
     */
    public List<PersonDTO> getPeople(String token) {
        validateAdminPermissions(token);
        return personRepository.findAll().stream().map(PersonMapper::toDTO).toList();
    }

    /**
     * Retrieves person information for a specific user.
     *
     * @param token the JWT token of the user making the request
     * @param username the username of the user whose information is being retrieved
     * @return a PersonDTO object with information about the person
     */
    public PersonDTO getPerson(String token, String username) {
        AppPerson person = validatePermissions(username, token, "READ");
        return PersonMapper.toDTO(person);
    }

    /**
     * Updates the information of a person.
     *
     * @param token the JWT token of the user making the request, the user is the same person whose information is being updated
     * @param personDTO the person DTO with updated information to be updated in the database
     * @param username the username of the user whose information is being updated
     */
    @Transactional
    public void updatePersonInfo(String token, PersonDTO personDTO, String username) {
        try {
            AppPerson personFromToken = validatePermissions(username, token, "UPDATE");
            AppPerson person = PersonMapper.toEntity(personDTO);

            String jsonBefore = JsonBuilderUtils.jsonBuilder(personFromToken);

            person.setAppUser(personFromToken.getAppUser());
            person.setId(personFromToken.getId());
            person.getAppUser().setId(personFromToken.getAppUser().getId());
            person.getAppUser().setAppPerson(person);

            logSenderService.sendLog(
                    null, null, null,
                    "Update", "User", "app_person", person.getId(),
                    "Person with:" + person.getId() + " ID has been updated successfully.",
                    jsonBefore,
                    JsonBuilderUtils.jsonBuilder(person)
            );

            personRepository.save(person);
        } catch (UserDoesNotExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Adds a new person.
     *
     * @param personDTO the person DTO to be added to the database.
     */
    @Transactional
    public void addPerson(PersonDTO personDTO) {
        try {
            AppPerson person = PersonMapper.toEntity(personDTO);

            logSenderService.sendLog(
                    null, null, null,
                    "Create", "User", "app_person", person.getId(),
                    "Person with:" + person.getId() + " ID has been created successfully.",
                    "{}",
                    JsonBuilderUtils.jsonBuilder(person)
            );

            personRepository.save(person);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Deletes a person.
     *
     * @param token the JWT token of the user making the request
     * @param id the ID of the person to be deleted
     */
    @Transactional
    public void deletePerson(String token, Integer id) {
        try {
            validateAdminPermissions(token);
            AppPerson person = personRepository.findById(id).orElseThrow(
                    () -> new UserDoesNotExistException("User does not exist")
            );

            logSenderService.sendLog(
                    null, null, null,
                    "Delete", "User", "app_person", id,
                    "Person with:" + id + " ID has been deleted successfully.",
                    JsonBuilderUtils.jsonBuilder(person),
                    "{}"
            );

            personRepository.delete(person);
        } catch (UserDoesNotExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Validates the permissions of a user making a request.
     * The user must have the required permission to perform the operation.
     * The permissions needed are specified in the permissionNeeded parameter.
     *
     * @param username the username of the user which information is being accessed
     * @param token the JWT token of the user making the request
     * @param permissionNeeded the required permission for the operation
     * @return the AppPerson object of the user which information is being accessed
     */
    private AppPerson validatePermissions(String username, String token, String permissionNeeded) {
        AppUser user = AuthorizationUtils.validatePermissions(username, token, permissionNeeded, jwtValidation, userRepository, rolePermissionRepository);
        return user.getAppPerson();
    }

    /**
     * Validates if the user has admin of full access permissions.
     *
     * @param token the JWT token of the user making the request
     */
    private void validateAdminPermissions(String token) {
        AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
    }
}