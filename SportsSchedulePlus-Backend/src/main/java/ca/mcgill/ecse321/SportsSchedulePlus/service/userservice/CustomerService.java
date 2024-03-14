package ca.mcgill.ecse321.SportsSchedulePlus.service.userservice;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.utils.Helper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public Person createCustomer(String name, String email, String password) {
    Helper.validateUser(personRepository, name, email, password,true);
    PersonRole personRole = new Customer();
    personRoleRepository.save(personRole);
    Person person = new Person(name, email, passwordEncoder.encode(password), personRole);
    personRepository.save(person);
    return person;
  }

  @Transactional
  public Person updateCustomer(int id, String name, String email, String password) {
    Optional <Person> optionalPerson = personRepository.findById(id);
    if (optionalPerson.isPresent()) {
      Person person = optionalPerson.get();
      if (person.getPersonRole() instanceof Customer) {
        boolean newEmail = !person.getEmail().equals(email);
        Helper.validateUser(personRepository, name, email, password,newEmail);
        person.setName(name);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(password));
        personRepository.save(person);
        return person;
      } else {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with ID " + id + " does not exist.");
      }
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not exist.");
    }
  }

  @Transactional
  public Customer getCustomer(int id) {
    Customer customer = customerRepository.findCustomerById(id);
    if (customer == null) {
      throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No customer with ID" + id + " found.");
    }
    return customer;
  }

  @Transactional
  public int deleteCustomer(int id) {
    Customer customer = customerRepository.findCustomerById(id);
    if (customer == null) {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
    } else {
      Optional <Person> optionalPerson = personRepository.findById(id);
      if (optionalPerson.isPresent()) {
        Person person = optionalPerson.get();
        if (person.getPersonRole() instanceof Customer) {
          int personId = person.getId();
          personRepository.delete(person);
          personRoleRepository.delete(person.getPersonRole());
          customerRepository.delete(customer);
          person.delete();
          return personId;
        } else {
          throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
        }
      } else {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with ID " + id + " does not exist.");
      }
    }
  }

  @Transactional
  public List <Customer> getAllCustomers() {
    return Helper.toList(customerRepository.findAll());
  }

}