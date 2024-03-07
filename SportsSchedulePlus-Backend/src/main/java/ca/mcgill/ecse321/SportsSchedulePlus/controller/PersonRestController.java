package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PersonResponseDTO;

@CrossOrigin(origins = "*")
@RestController
public class PersonRestController {

    @Autowired
    private PersonService personService;

    /*
     * get all persons (all users)
     */
    @GetMapping(value = {"/persons", "/persons/"})
    public List<PersonResponseDTO> getAllPersons() {
        return personService.getAllPersons().stream().map(p ->
        new PersonResponseDTO(p)).collect(Collectors.toList());
    }

    /*
     * get person by their person id
     */
    @GetMapping(value = {"/persons/{id}", "/persons/{id}/"})
    public PersonResponseDTO getPerson(@PathVariable("id") int id) {
        Person p = personService.getPersonById(id);
        return new PersonResponseDTO(p);
    }
}