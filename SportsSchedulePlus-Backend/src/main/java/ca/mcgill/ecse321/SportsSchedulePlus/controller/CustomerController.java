package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.CustomerService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  public List < PersonResponseDTO > getAllCustomers() {
    return customerService.getAllCustomers().stream().map(customer ->convertToDto(customer)).collect(Collectors.toList());
  }

  @GetMapping(value = {"/customers/{id}"})
  public PersonResponseDTO getCustomer(@PathVariable("id") int id) {
    Customer customer = customerService.getCustomer(id);
    return convertToDto(customer);
  }

  @DeleteMapping(value = {"/customers/{id}"})
  public ResponseEntity<String>  deleteCustomer(@PathVariable("id") int id) {
    int personId = customerService.deleteCustomer(id);
    return ResponseEntity.ok("Customer with id " + personId + " was successfully deleted.");
  }

  @PostMapping(value = {"/customers"})
  public PersonResponseDTO createCustomer(@RequestBody PersonResponseDTO personDto) {
    Person person = customerService.createCustomer(personDto.getName(), personDto.getEmail(), personDto.getPassword());
    return convertToDto(person);
  }

  @PutMapping(value = {"/customers/{id}"})
  public PersonResponseDTO updateCustomer(@PathVariable("id") int id, @RequestBody PersonResponseDTO personDto) {
    Person person = customerService.updateCustomer(id, personDto.getName(), personDto.getEmail(), personDto.getPassword());
    return convertToDto(person);
  }

  private PersonResponseDTO convertToDto(Person p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such customer!");
    }
    int cId = p.getId();
    Customer c = customerService.getCustomer(cId);
    CustomerResponseDTO customerDto = new CustomerResponseDTO(c);
    PersonResponseDTO personDto = new PersonResponseDTO(p.getName(), p.getEmail(), p.getPassword(), customerDto);
    return personDto;
  }

  private PersonResponseDTO convertToDto(Customer c) {
    if (c == null) {
      throw new IllegalArgumentException("There is no such customer!");
    }
    Person person = personService.getPersonById(c.getId());
    CustomerResponseDTO customerDto = new CustomerResponseDTO(c);
    PersonResponseDTO personDto = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), customerDto);
    return personDto;
  }
}