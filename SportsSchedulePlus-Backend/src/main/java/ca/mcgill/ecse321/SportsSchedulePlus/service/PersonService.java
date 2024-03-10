package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.utils.Helper;

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;

    @Autowired CustomerRepository customerRepository;

    @Transactional
    public List<Person> getAllPersons() {
        return Helper.toList(personRepository.findAll());
    }

    @Transactional
    public Person getPersonById(int id) {
        Optional<Person> p = personRepository.findById(id);
        if (!p.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + ".");
        }
        return p.get();
    }

    

    @Transactional
    public void deletePersonById(int id) {
        System.out.println("Deleting person with ID: " + id);
    
        Optional<Person> existingPerson = personRepository.findById(id);
    
        if (!existingPerson.isPresent()) {
            System.out.println("Person with ID " + id + " not found. Unable to delete.");
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + " to delete.");
        }
    
        personRepository.deleteById(id);
        System.out.println("Person with ID " + id + " successfully deleted.");
    }

    @Transactional
    public Person findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }
    

  
}