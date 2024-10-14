package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppPerson;
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
    
    public List<AppPerson> getPeople() {
        return personRepository.findAll();
    }

    public AppPerson getPerson(String username) {
        Integer id = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        ).getId();
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addNewPerson(AppPerson person) {
        if (personRepository.findById(person.getId()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        personRepository.save(person);
    }

    @Transactional
    public void updatePersonInfo(AppPerson person) {
        AppPerson personToUpdate = personRepository.findById(person.getId()).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        if (personToUpdate.equals(person)) {
            throw new UserAlreadyExistsException("Same Information");
        }
        personRepository.save(person);
    }
}
