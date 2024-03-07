package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.CustomerDto;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.service.CustomerService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class CustomerController {

    @Autowired
    private PersonService personService;

    @PostMapping("/customer")
    public ResponseEntity<CustomerDto> createCustomer() {
        // Convert the DTO back to the entity
        Customer createdCustomer = personService.createCustomer();
        // Convert the created entity back to DTO
        CustomerDto createdCustomerDto = new CustomerDto(createdCustomer);

        return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
    }
}