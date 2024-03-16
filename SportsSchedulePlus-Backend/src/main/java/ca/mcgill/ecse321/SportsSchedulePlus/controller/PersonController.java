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
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;

@CrossOrigin(origins = "*")
@RestController
public class PersonController {

    @Autowired
    private UserService userService;

    /*
     * get all persons (all users)
     */
    @GetMapping(value = {"/persons", "/persons/"})
    public PersonListResponseDTO getAllPersons() {
        List<PersonResponseDTO> personResponseDTOS = new ArrayList<>();
        for (Person p : userService.getAllPersons()) {
            personResponseDTOS.add(new PersonResponseDTO(p));
        }
        return new PersonListResponseDTO(personResponseDTOS);
    }

    /*
     * get person by their person id
     */
    @GetMapping(value = {"/persons/{id}", "/persons/{id}/"})
    public PersonResponseDTO getPerson(@PathVariable("id") int id) {
        Person p = userService.getPersonById(id);
        return new PersonResponseDTO(p);
    }
}