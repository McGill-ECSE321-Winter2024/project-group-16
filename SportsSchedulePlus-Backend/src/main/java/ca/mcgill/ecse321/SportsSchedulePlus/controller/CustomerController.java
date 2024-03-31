package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Rest Controller that handles CRUD on Customer
 */
@CrossOrigin(origins = "http://localhost:8087")
@RestController
public class CustomerController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all customers
     * @return PersonListResponseDTO
     */
    @GetMapping(value = {"/customers"})
    public PersonListResponseDTO getAllCustomers() {
        List<PersonDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : userService.getAllCustomers()) {
            Person person = userService.getPersonById(customer.getId());
            customerDTOs.add(new PersonDTO(person));
        }
        return new PersonListResponseDTO(customerDTOs);
    }
    /**
     * Retrieves the customer by the path variable id
     * @param id
     * @return PersonDTO
     */
    @GetMapping(value = {"/customers/{id}"})
    public PersonDTO getCustomer(@PathVariable("id") int id) {
        Person person = userService.getPersonById(id);
        return new PersonDTO(person);
    }

     /**
     * Retrieves the customer by the path variable email
     * @param id
     * @return PersonDTO
     */
    @GetMapping(value = {"/customers/email/{email}"})
    public PersonDTO getCustomer(@PathVariable("email") String email) {
        Person person = userService.findPersonByEmail(email);
        return new PersonDTO(person);
    }
    
    /**
     * Deletes the customer by the path variable id
     * @param id
     * @return String response entity
     */
    @DeleteMapping(value = {"/customers/{id}"})
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") int id) {
        int personId = userService.deleteUser(id);
        return ResponseEntity.ok("Customer with id " + personId + " was successfully deleted.");
    } 
    
    /**
     * Creates a customer with the information in the request body
     * @param personDto
     * @return PersonDTO
     */
    @PostMapping(value = {"/customers"})
    public PersonDTO createCustomer(@RequestBody PersonDTO personDto) {
        Person person = userService.createCustomer(personDto.getName(), personDto.getEmail(), personDto.getPassword());
        return new PersonDTO(person);
    }
    
    /**
     * Updates the customer with the path variable id with the request body information
     * @param id
     * @param personDto
     * @return PersonDTO 
     */
    @PutMapping(value = {"/customers/{id}"})
    public PersonDTO updateCustomer(@PathVariable("id") int id, @RequestBody PersonDTO personDto) {
        Person person = userService.updateUser(id, personDto.getName(), personDto.getEmail(), personDto.getPassword(), "");
        return new PersonDTO(person);
    }
    
    /**
     * Allows the customer to apply to become an instructor
     * @param customerId
     * @return PersonDTO
     */
    @PutMapping(value = {"/customers/{customerId}/apply"})
    public PersonDTO applyForInstructor(@PathVariable("customerId") int customerId) {
        userService.applyForInstructor(customerId);
        Person person = userService.getPersonById(customerId);
        return new PersonDTO(person);
    }
    
    /**
     * Approves a customer to become an instructor
     * @param customerId
     * @return PersonDTO
     */
    @PutMapping(value = {"/customers/{customerId}/approve"})
    public PersonDTO approveCustomer(@PathVariable("customerId") int customerId) {
        userService.approveCustomer(customerId);
        Person person = userService.getPersonById(customerId);
        return new PersonDTO(person);
    }
    
    /**
     * Rejects a customer's request to become an instructor
     * @param customerId
     * @return PersonDTO
     */
    @PutMapping(value = {"/customers/{customerId}/reject"})
    public PersonDTO rejectCustomer(@PathVariable("customerId") int customerId) {
        userService.rejectCustomer(customerId);
        Person person = userService.getPersonById(customerId);
        return new PersonDTO(person);
    }

}