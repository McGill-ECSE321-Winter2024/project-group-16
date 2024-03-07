package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
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
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + ".");
        }
        return p.get();
    }

    @Transactional
public Customer createCustomer() {
    
    String name = "Dania";
    String email = "dania.bouhmidi@gmail.com";

    // Create a new Person object
    Person person = new Person();
    person.setName(name);
    person.setEmail(email);
   

    // Create a new Customer (PersonRole) object
    Customer customer = new Customer();
    
    customerRepository.save(customer);
    // Set the PersonRole for the Person
    person.setPersonRole(customer);

    // Save both the Person and the associated PersonRole to the database
     personRepository.save(person);
     return customer;
}
}
