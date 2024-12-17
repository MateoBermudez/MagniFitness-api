package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.PersonDTO;
import com.devcrew.usermicroservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is the controller class for the Person entity.
 * It contains the endpoints for the Person entity.
 * The endpoints are used to get, add, update, and delete a person.
 * The endpoints are secured with the @PreAuthorize annotation to restrict access to the endpoints.
 * The endpoints are secured based on the role of the user.
 */
@RestController
@RequestMapping(path = "api/person")
public class PersonController {

    /**
     * The person service is used to perform operations on the person entity.
     */
    private final PersonService personService;

    /**
     * The constructor is used to inject the person service into the person controller.
     * @param personService The person service to be injected into the person controller.
     */
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * This endpoint is used to get all the people in the system.
     * @param token The token of the user making the request.
     * @return A response entity containing the list of people in the system.
     */
    //Only admin can get all-people information
    @GetMapping(path = "/get-all")
    public ResponseEntity<Object> getPeople(@RequestHeader("Authorization") String token) {
        List<PersonDTO> people = personService.getPeople(token);
        return ResponseEntity.ok(people);
    }

    /**
     * This endpoint is used to get the information of a person.
     * @param token The token of the user making the request.
     * @param username The username of the person whose information is to be retrieved.
     * @return A response entity containing the information of the person.
     */
    //User is null, to make user not null, they use the authentication controller -> Register into the system
    //Only admin can get the information of any person, and user can get his own information
    @GetMapping(path = "/info/{username}")
    public ResponseEntity<Object> getPerson(@RequestHeader("Authorization") String token, @PathVariable String username) {
        PersonDTO person = personService.getPerson(token, username);
        return ResponseEntity.ok(person);
    }

    /**
     * This endpoint is used to add a new person to the system.
     * @param personDTO The person to be added to the system.
     * @return A response entity indicating that the person has been added to the system.
     */
    //Any user can add a new person without any restrictions -> Add restrictions if needed; This endpoint needs revision
    @PostMapping(path = "/add")
    public ResponseEntity<Object> addPerson(@RequestBody PersonDTO personDTO) {
        personService.addPerson(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This endpoint is used to update the information of a person.
     * @param token The token of the user making the request.
     * @param personDTO The updated information of the person.
     * @param username The username of the person whose information is to be updated.
     * @return A response entity indicating that the information of the person has been updated.
     */
    //Only admin can update any person's information, and a user can update their own information
    @PutMapping(path = "/update/{username}")
    public ResponseEntity<Object> updatePersonInformation(@RequestHeader("Authorization") String token, @RequestBody PersonDTO personDTO, @PathVariable String username) {
        personService.updatePersonInfo(token, personDTO, username);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to delete a person from the system.
     * @param token The token of the user making the request.
     * @param id The id of the person to be deleted.
     * @return A response entity indicating that the person has been deleted from the system.
     */
    //Only admin can delete any person
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Object> deletePerson(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        personService.deletePerson(token, id);
        return ResponseEntity.noContent().build();
    }
}