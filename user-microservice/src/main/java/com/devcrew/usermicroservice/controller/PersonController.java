package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path = "/get-all")
    public List<PersonDTO> getPeople() {
        return personService.getPeople();
    }

    @GetMapping(path = "/info/{username}")
    public PersonDTO getPerson(@PathVariable String username) {
        return personService.getPerson(username);
    }

    //Post gets handled by User Controller, every new User has personal information, that's why its handled there

    @PutMapping(path = "/update/{username}")
    public void updatePersonInformation(@RequestBody PersonDTO personDTO, @PathVariable String username) {
        personService.updatePersonInfo(personDTO, username);
    }

    //Deletion gets handled by User Controller. If User with the same Username is deleted, the Person with the same Username will be deleted as well
}
