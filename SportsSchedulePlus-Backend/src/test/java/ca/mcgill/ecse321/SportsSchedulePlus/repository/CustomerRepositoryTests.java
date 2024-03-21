package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
/**
 * This class contains unit tests for the CustomerRepository.
 * The overridden equals method in the Customer model is used for assertions.
 */
@SpringBootTest
public class CustomerRepositoryTests {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private PersonRoleRepository personRoleRepository;
    @Autowired
    private PersonRepository personRepository;


    /**
     * Clear the database after each test to ensure a clean state.
     */
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        personRoleRepository.deleteAll();
        personRepository.deleteAll();
    }
    
    /**
     * Test finding a customer by ID.
     */
    @Test
    public void testFindCustomerById() {
        // Create and save Customer
        Customer customer = new Customer();
        customerRepository.save(customer);

        // Load Customer from database
        Customer loadedCustomer = customerRepository.findCustomerById(customer.getId());

        // Asserts using overridden equals method in the Customer model
        assertEquals(customer, loadedCustomer);
    }

     /**
     * Test finding a customer by ID when the customer is not found.
     */
    @Test
    public void testFindCustomerByIdNotFound() {
        // Try to load a non-existing Customer from the database
        Customer loadedCustomer = customerRepository.findCustomerById(12345);

        // Assert that the loaded customer is null
        assertNull(loadedCustomer);
    }


}
