package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.CustomerDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PersonDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PersonRoleDto;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.service.CustomerService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PersonService personService;

    @GetMapping(value={"/customers"})
    public List<PersonDto> getAllCustomers(){
        return customerService.getAllCustomers().stream().map
                (customer -> convertToDto(customer)).collect(Collectors.toList());
    }

    @GetMapping(value={"/customers/{id}"})
    public PersonDto getCustomer(@PathVariable("id") int id){
        Customer customer = customerService.getCustomer(id);
        return convertToDto(customer);
    }

    @DeleteMapping(value={"/customers/{id}"})
    public PersonDto deleteCustomer(@PathVariable("id") int id){
        Customer customer = customerService.deleteCustomer(id);
        return convertToDto(customer);
    }

    @PostMapping(value={"/customers"})
    public PersonDto createCustomer(@RequestBody PersonDto personDto){
        Person person = customerService.createCustomer(personDto.getName(),
                personDto.getEmail(), personDto.getPassword());
        return convertToDto(person);
    }

    @PutMapping(value={"/customers/{id}"})
    public PersonDto updateCustomer(@PathVariable("id") int id, @RequestBody PersonDto personDto){
        Person person = customerService.updateCustomer(personDto.getName(),
                personDto.getEmail(), personDto.getPassword(),id);
        return convertToDto(person);
    }

    private PersonDto convertToDto(Person p){
        if (p == null){
            throw new IllegalArgumentException("There is no such customer!");
        }
        PersonRoleDto personRoleDto = new CustomerDto();
        PersonDto customerDto = new PersonDto(p.getName(), p.getEmail(), p.getPassword(),personRoleDto);
        return customerDto;
    }

    private PersonDto convertToDto(Customer c){
        if (c == null){
            throw new IllegalArgumentException("There is no such customer!");
        }
        Person person = personService.getPersonById(c.getId());
        PersonDto personDto = new PersonDto(person.getName(), person.getEmail(), person.getPassword());
        return personDto;

    }

}
