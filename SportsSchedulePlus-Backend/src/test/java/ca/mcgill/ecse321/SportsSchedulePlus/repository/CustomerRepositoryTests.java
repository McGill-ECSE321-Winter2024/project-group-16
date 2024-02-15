package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PersonRoleRepository personRoleRepository;

    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        personRoleRepository.deleteAll();
    }

    // Test for finding customer by id
    @Test
    public void findCustomerById() {
        // Create and save Customer
        Customer customer = new Customer(1);
        customerRepository.save(customer);

        // Load Customer from database
        Customer loadedCustomer = (Customer) personRoleRepository.findPersonRoleById(1);

        // Asserts
        assertNotNull(loadedCustomer);
        assertEquals(customer, loadedCustomer);

    }
}
