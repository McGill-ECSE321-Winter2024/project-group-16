package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class CustomerController {

    @Autowired
    private UserService userService;


    @GetMapping(value = {"/customers"})
    public List<PersonResponseDTO> getAllCustomers() {
        return userService.getAllCustomers().stream().map(customer -> convertToDto(customer)).collect(Collectors.toList());
    }

    @GetMapping(value = {"/customers/{id}"})
    public PersonResponseDTO getCustomer(@PathVariable("id") int id) {
        Customer customer = userService.getCustomer(id);
        return convertToDto(customer);
    }

    @DeleteMapping(value = {"/customers/{id}"})
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") int id) {
        int personId = userService.deleteUser(id);
        return ResponseEntity.ok("Customer with id " + personId + " was successfully deleted.");
    }

    @PostMapping(value = {"/customers"})
    public PersonResponseDTO createCustomer(@RequestBody PersonResponseDTO personDto) {
        Person person = userService.createCustomer(personDto.getName(), personDto.getEmail(), personDto.getPassword());
        return convertToDto(person);
    }

    @PutMapping(value = {"/customers/{id}"})
    public PersonResponseDTO updateCustomer(@PathVariable("id") int id, @RequestBody PersonResponseDTO personDto) {
        Person person = userService.updateUser(id, personDto.getName(), personDto.getEmail(), personDto.getPassword(), "");
        return convertToDto(person);
    }

    @PutMapping(value = {"/customers/{customerId}/apply"})
    public PersonResponseDTO applyForInstructor(@PathVariable("customerId") int customerId) {
        Customer c = userService.applyForInstructor(customerId);
        return convertToDto(c);
    }

    @PutMapping(value = {"/customers/{customerId}/approve"})
    public PersonResponseDTO approveCustomer(@PathVariable("customerId") int customerId) {
        return convertToDto(userService.approveCustomer(customerId));
    }

    @PutMapping(value = {"/customers/{customerId}/reject"})
    public void rejectCustomer(@PathVariable("customerId") int customerId) {
        userService.rejectCustomer(customerId);
    }

    private PersonResponseDTO convertToDto(Person p) {
        if (p == null) {
            throw new IllegalArgumentException("There is no such customer!");
        }
        int cId = p.getId();
        Customer c = userService.getCustomer(cId);
        CustomerResponseDTO customerDto = new CustomerResponseDTO(c);
        return (new PersonResponseDTO(p.getName(), p.getEmail(), p.getPassword(), customerDto));
    }

    private PersonResponseDTO convertToDto(Customer c) {
        if (c == null) {
            throw new IllegalArgumentException("There is no such customer!");
        }
        Person person = userService.getPersonById(c.getId());
        CustomerResponseDTO customerDto = new CustomerResponseDTO(c);
        return (new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), customerDto));
    }
    private PersonResponseDTO convertToDto(Instructor i) {
        if (i == null) {
            throw new IllegalArgumentException("There is no such customer!");
        }
        Person person = userService.getPersonById(i.getId());
        InstructorResponseDTO instructorDto = new InstructorResponseDTO(i);
        return (new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), instructorDto));
    }
}