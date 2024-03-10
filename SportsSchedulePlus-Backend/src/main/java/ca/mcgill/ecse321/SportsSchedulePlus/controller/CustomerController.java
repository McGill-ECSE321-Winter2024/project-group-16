package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.CustomerDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.service.CustomerService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.* ;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")@RestController
public class CustomerController {

  @Autowired
  private CustomerService customerService;
  @Autowired
  private PersonService personService;



  @GetMapping(value = { "/customers"})
  public List < PersonDTO > getAllCustomers() {
    return customerService.getAllCustomers().stream().map(customer ->convertToDto(customer)).collect(Collectors.toList());
  }

  @GetMapping(value = {"/customers/{id}"})
  public PersonDTO getCustomer(@PathVariable("id") int id) {
    Customer customer = customerService.getCustomer(id);
    return convertToDto(customer);
  }

  //not working
  @DeleteMapping(value = {"/customers/{id}"})
  public String deleteCustomer(@PathVariable("id") int id) {
    int personId = customerService.deleteCustomer(id);
    return ("Customer with id " + personId + " was successfully deleted.");
  }

  @PostMapping(value = {"/customers"})
  public PersonDTO createCustomer(@RequestBody PersonDTO personDto) {
    Person person = customerService.createCustomer(personDto.getName(), personDto.getEmail(), personDto.getPassword());
    return convertToDto(person);
  }

  @PutMapping(value = {"/customers/{id}"})
  public PersonDTO updateCustomer(@PathVariable("id") int id, @RequestBody PersonDTO personDto) {
    Person person = customerService.updateCustomer(id, personDto.getName(), personDto.getEmail(), personDto.getPassword());
    return convertToDto(person);
  }

  private PersonDTO convertToDto(Person p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such customer!");
    }
    int cId = p.getId();
    Customer c = customerService.getCustomer(cId);
    CustomerDTO customerDto = new CustomerDTO(c);
    PersonDTO personDto = new PersonDTO(p.getName(), p.getEmail(), p.getPassword(), customerDto);
    return personDto;
  }

  private PersonDTO convertToDto(Customer c) {
    if (c == null) {
      throw new IllegalArgumentException("There is no such customer!");
    }
    Person person = personService.getPersonById(c.getId());
    CustomerDTO customerDto = new CustomerDTO(c);
    PersonDTO personDto = new PersonDTO(person.getName(), person.getEmail(), person.getPassword(), customerDto);
    return personDto;

  }



}