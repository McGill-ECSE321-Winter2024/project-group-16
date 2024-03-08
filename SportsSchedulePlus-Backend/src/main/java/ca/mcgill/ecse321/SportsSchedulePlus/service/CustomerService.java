package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.utils.EmailValidator;
import ca.mcgill.ecse321.utils.Helper;
import ca.mcgill.ecse321.utils.PasswordValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonRoleRepository personRoleRepository;

    @Transactional
    public Person createCustomer(String name, String email, String password){
        validateUser(name, email, password);
        PersonRole personRole = new Customer();
        personRoleRepository.save(personRole);
        Person person = new Person(name, email, password, personRole);
        personRepository.save(person);
        return person;
    }

    @Transactional
    public Person updateCustomer(String name, String email, String password, int id){
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Customer) {
                person.setName(name);
                person.setEmail(email);
                person.setPassword(password);
                personRepository.save(person);
                return person;
            } else{
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
            }
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with ID "+ id + " does not exist.");
        }
    }

    @Transactional
    public Customer getCustomer(int id){
        Customer customer = customerRepository.findCustomerById(id);
        if(customer == null){
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No customer with ID" + id + " found.");
        }
        return customer;
    }

    @Transactional
    public int deleteCustomer(int id){
        Customer customer = customerRepository.findCustomerById(id);
        if(customer == null){
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
        }
        else{
        Optional<Person> optionalPerson = personRepository.findById(id);
        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Customer) {
                int personId = person.getId();
                System.out.println(person.getEmail());
                personRepository.delete(person);
                personRoleRepository.delete(person.getPersonRole());
                customerRepository.delete(customer);
                System.out.println("CPERSON DELETED.");
                person.delete();
                return personId;
            } else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
            }
        }else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with ID "+ id + " does not exist.");
            }
        }
    }
    @Transactional
    public List<Customer> getAllCustomers(){
        return Helper.toList(customerRepository.findAll());

    }

    private void validateUser(String name, String email, String password) {
        if (personRepository.findPersonByEmail(email) != null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "User with email " + email + " already exists.");
        }
    
        if (name.isBlank()) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Name cannot be blank.");
        }
    
        if (!PasswordValidator.isValidPassword(password)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Password is not valid.");
        }
    
        if (!EmailValidator.validate(email)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Email is not valid.");
        }
    }

}