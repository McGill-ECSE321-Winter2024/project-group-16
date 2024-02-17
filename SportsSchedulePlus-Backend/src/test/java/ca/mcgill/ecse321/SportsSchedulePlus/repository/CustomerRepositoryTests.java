package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;

@SpringBootTest

public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;


    @BeforeEach
    @AfterEach
    public void setUp() {
        // Clear the database before each test
        customerRepository.deleteAll();
    }

    @Test
    public void testSaveCustomer() {
        // Create a customer
        Customer customer = new Customer();
        // Save the customer to the repository
        customerRepository.save(customer);

        PersonRole retrievedCustomer =  customerRepository.findCustomerById(customer.getId());

        // Verify that the customer was saved and retrieved successfully
        assertNotNull(retrievedCustomer);
        assertEquals(customer,retrievedCustomer);
    }
/* 
    @Test
    public void testFindCustomerById() {
        // Create a customer
        PersonRole customer = new Customer();
        customer.setId(2);
        // Save the customer to the repository
        customerRepository.save((Customer)customer);

        // Find the customer by ID
        Customer foundCustomer = customerRepository.findCustomerById(2);

        // Verify that the correct customer was found
        assertNotNull(foundCustomer);
    }
*/
    @Test
    public void testFindCustomerById_NotFound() {
        // Try to find a non-existent customer by ID
       // Customer foundCustomer = customerRepository.findCustomerById(0);

        // Verify that null is returned for a non-existent customer
       // assertNull(foundCustomer);
    }
}
