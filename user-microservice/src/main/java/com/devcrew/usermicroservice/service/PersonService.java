package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    public List<AppPerson> getPeople() {
        return personRepository.findAll();
    }

    public AppPerson getPerson(String username) {
        return personRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void addNewPerson(AppPerson person) {
        if (personRepository.findByUsername(person.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        personRepository.save(person);
    }

    @Transactional
    public void updatePersonInfo(AppPerson person) {
        AppPerson personToUpdate = personRepository.findByUsername(person.getUsername()).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        if (personToUpdate.equals(person)) {
            throw new UserAlreadyExistsException("Same Information");
        }
        personRepository.save(person);
    }
}
