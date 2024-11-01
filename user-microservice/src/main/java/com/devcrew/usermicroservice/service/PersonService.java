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
import com.devcrew.usermicroservice.utils.JwtValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final JwtValidation jwtValidation;
    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, UserRepository userRepository, JwtValidation jwtValidation, RolePermissionRepository rolePermissionRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.jwtValidation = jwtValidation;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public List<PersonDTO> getPeople(String token) {
        validateAdminPermissions(token);
        return personRepository.findAll().stream().map(PersonMapper::toDTO).toList();
    }

    public PersonDTO getPerson(String token, String username) {
        AppPerson person = validatePermissions(username, token, "READ");
        return PersonMapper.toDTO(person);
    }

    @Transactional
    public void updatePersonInfo(String token, PersonDTO personDTO, String username) {
        try {
            AppPerson personFromToken = validatePermissions(username, token, "WRITE, EDIT, UPDATE");
            AppPerson person = PersonMapper.toEntity(personDTO);

            person.setAppUser(personFromToken.getAppUser());
            person.setId(personFromToken.getId());
            person.getAppUser().setId(personFromToken.getAppUser().getId());
            person.getAppUser().setAppPerson(person);

            personRepository.save(person);
        } catch (UserDoesNotExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void addPerson(PersonDTO personDTO) {
        try {
            AppPerson person = PersonMapper.toEntity(personDTO);
            personRepository.save(person);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void deletePerson(String token, Integer id) {
        try {
            validateAdminPermissions(token);
            AppPerson person = personRepository.findById(id).orElseThrow(
                    () -> new UserDoesNotExistException("User does not exist")
            );
            personRepository.delete(person);
        } catch (UserDoesNotExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private AppPerson validatePermissions(String username, String token, String permissionNeeded) {
        AppUser user = AuthorizationUtils.validatePermissions(username, token, permissionNeeded, jwtValidation, userRepository, rolePermissionRepository);
        return user.getAppPerson();
    }

    private void validateAdminPermissions(String token) {
        AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
    }
}