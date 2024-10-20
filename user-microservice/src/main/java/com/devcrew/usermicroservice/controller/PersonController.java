package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    //Only admin can get all-people information
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get-all")
    public List<PersonDTO> getPeople() {
        return personService.getPeople();
    }

    //User is null, to make user not null, they use the authentication controller -> Register into the system
    //Only admin can get the information of any person, and user can get his own information
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @GetMapping(path = "/info/{username}")
    public PersonDTO getPerson(@PathVariable String username) {
        return personService.getPerson(username);
    }

    //Any user can add a new person without any restrictions
    @PostMapping(path = "/add")
    public void addPerson(@RequestBody PersonDTO personDTO) {
        personService.addPerson(personDTO);
    }

    //Only admin can update any person's information, and a user can update their own information
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @PutMapping(path = "/update/{username}")
    public void updatePersonInformation(@RequestBody PersonDTO personDTO, @PathVariable String username) {
        personService.updatePersonInfo(personDTO, username);
    }

    //Only admin can delete any person
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public void deletePerson(@PathVariable Integer id) {
        personService.deletePerson(id);
    }
}
