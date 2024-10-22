package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.exception.BadRequestException;
import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.mapper.PersonMapper;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }
    
    public List<PersonDTO> getPeople() {
        return personRepository.findAll().stream().map(PersonMapper::toDTO).toList();
    }

    public PersonDTO getPerson(String username) {
        Integer id = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        ).getId();
        AppPerson person = personRepository.findById(id).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        return PersonMapper.toDTO(person);
    }

    @Transactional
    public void updatePersonInfo(PersonDTO personDTO, String username) {
        AppPerson person = PersonMapper.toEntity(personDTO);

        Integer user_id = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        ).getId();

        AppUser user = userRepository.findById(user_id).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        AppPerson personToUpdate = personRepository.findById(user_id).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        if (!username.equals(user.getUsername())) {
            throw new BadRequestException("Information does not match");
        }

        if (personToUpdate.equals(person)) {
            throw new UserAlreadyExistsException("Same Information");
        }

        person.setAppUser(user);
        person.setId(user_id);
        person.getAppUser().setId(user_id);

        personRepository.save(person);
    }

    @Transactional
    public void addPerson(PersonDTO personDTO) {
        AppPerson person = PersonMapper.toEntity(personDTO);

        personRepository.save(person);
    }

    @Transactional
    public void deletePerson(Integer id) {
        AppPerson person = personRepository.findById(id).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        personRepository.delete(person);
    }
}
