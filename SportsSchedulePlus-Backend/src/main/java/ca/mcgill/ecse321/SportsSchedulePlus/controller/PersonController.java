package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;

/*
 * Rest Controller that handles CRUD on Person
 */
@CrossOrigin(origins = "http://localhost:8087")
@RestController
public class PersonController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all persons
     * @return PersonListResponseDTO
     */
    @GetMapping(value = {"/persons", "/persons/"})
    public PersonListResponseDTO getAllPersons() {
        List<PersonDTO> personResponseDTOS = new ArrayList<>();
        for (Person person : userService.getAllPersons()) {
            personResponseDTOS.add(new PersonDTO(person));
        }
        return new PersonListResponseDTO(personResponseDTOS);
    }

   /**
    * Retrieves a person by the path variable id
    * @param id
    * @return PersonDTO 
    */
    @GetMapping(value = {"/persons/{id}", "/persons/{id}/"})
    public PersonDTO getPerson(@PathVariable("id") int id) {
        Person person = userService.getPersonById(id);
        return new PersonDTO(person);
    }
}