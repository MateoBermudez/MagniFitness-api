package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.exception.BadRequestException;
import com.devcrew.usermicroservice.exception.ForbiddenException;
import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.mapper.PersonMapper;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.JwtValidation;
import com.devcrew.usermicroservice.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final JwtValidation jwtValidation;


    @Autowired
    public PersonService(PersonRepository personRepository, UserRepository userRepository, JwtValidation jwtValidation) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.jwtValidation = jwtValidation;
    }
    
    public List<PersonDTO> getPeople(String token) {
        ValidateAdmin(token);
        return personRepository.findAll().stream().map(PersonMapper::toDTO).toList();
    }

    public PersonDTO getPerson(String token, String username) {
        AppPerson person = ValidateAuthorizationForAdminAndUser(username, token);
        return PersonMapper.toDTO(person);
    }

    //Revise this method -> Can fail due to change in PK and FK in user table
    @Transactional
    public void updatePersonInfo(String token, PersonDTO personDTO, String username) {
        AppPerson personFromToken = ValidateAuthorizationForAdminAndUser(username, token);
        AppPerson person = PersonMapper.toEntity(personDTO);

        person.setAppUser(personFromToken.getAppUser());
        person.setId(personFromToken.getId());
        person.getAppUser().setId(personFromToken.getAppUser().getId());
        person.getAppUser().setAppPerson(person);

        personRepository.save(person);
    }

    //This needs to be revised
    @Transactional
    public void addPerson(PersonDTO personDTO) {
        AppPerson person = PersonMapper.toEntity(personDTO);

        personRepository.save(person);
    }

    @Transactional
    public void deletePerson(String token, Integer id) {
        ValidateAdmin(token);
        AppPerson person = personRepository.findById(id).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        personRepository.delete(person);
    }

    private AppPerson ValidateAuthorizationForAdminAndUser(String username, String token) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);
        String usernameFromToken = jwtValidation.validateUsernameFromToken(token);

        if (!(roleFromToken.equals("ADMIN") || usernameFromToken.equals(username))) {
            throw new ForbiddenException("User does not have permission");
        }
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        ).getAppPerson();
    }

    private void ValidateAdmin(String token) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);

        if (!roleFromToken.equals("ADMIN")) {
            throw new ForbiddenException("User does not have permission");
        }
    }
}
