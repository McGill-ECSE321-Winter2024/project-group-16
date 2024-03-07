package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.util.SportsScheduleException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PersonRepository personRepository;

    // Validation on customer inputs
    @Transactional
    public Person createCustomer(String name, String email, String password){
        PersonRole personRole = new Customer();
        Person person = new Person(name, email, password, personRole);
        personRepository.save(person);
        return person;
    }

    // Validation on customer inputs
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
                throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
            }
        } else {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ id + " does not exist.");
        }
    }

    @Transactional
    public Customer getCustomer(int id){
        Customer customer = customerRepository.findCustomerById(id);
        return customer;
    }

    @Transactional
    public Customer deleteCustomer(int id){
        Customer customer = customerRepository.findCustomerById(id);
        customerRepository.delete(customer);
        return customer;
    }
    @Transactional
    public List<Customer> getAllCustomers(){
        return toList(customerRepository.findAll());

    }


    private <T> List<T> toList(Iterable<T> iterable){
        List<T> resultList = new ArrayList<T>();
        for (T t: iterable){
            resultList.add(t);
        }
        return resultList;
    }

    // should i add validation for password and email
    private void validateCustomer(String name, String email, String password, Integer id){


    }


}
