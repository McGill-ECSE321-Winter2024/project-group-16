package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.utils.Helper;

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public List<Person> getAllPersons() {
        return Helper.toList(personRepository.findAll());
    }

    @Transactional
    public Person getPersonById(int id) {
        Optional<Person> p = personRepository.findById(id);
        if (!p.isPresent()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + ".");
        }
        return p.get();
    }
}
