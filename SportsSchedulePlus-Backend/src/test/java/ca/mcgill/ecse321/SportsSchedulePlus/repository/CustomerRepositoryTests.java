package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.util.Helper;
/**
 * This class contains unit tests for the CustomerRepository.
 * The overridden equals method in the Customer model is used for assertions.
 */
@SpringBootTest
public class CustomerRepositoryTests {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Clear the database after each test to ensure a clean state.
     */
    @AfterEach
    public void clearDatabase() {
        paymentRepository.deleteAll();
        customerRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
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



    /**
     * Test saving a customer with associated scheduled courses.
     */
    @Test
    public void testSaveCustomerWithScheduledCourses() {
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse firstCourse = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(firstCourse);
        ScheduledCourse secondCourse = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(secondCourse);
        Customer customer = new Customer();
        customer.addCoursesRegistered(firstCourse);
        customer.addCoursesRegistered(secondCourse);

        customerRepository.save(customer);

        Customer loadedCustomer = customerRepository.findCustomerById(customer.getId());

        // Asserts using overridden equals method in the Customer model
        assertEquals(customer, loadedCustomer);
    }

    /**
     * Test saving a customer with associated payments.
     */
    @Test
    public void testSaveCustomerWithPayments() {
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse firstCourse = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(firstCourse);
        customer.addCoursesRegistered(firstCourse);

        Payment firstPayment = Helper.createPayment(customer,firstCourse);
        firstPayment.setKey(new Payment.Key(customer, firstCourse));
        paymentRepository.save(firstPayment);
        customer.addCustomerPayment(firstPayment);
        customerRepository.save(customer);

        Customer loadedCustomer = customerRepository.findCustomerById(customer.getId());

        // Asserts using overridden equals method in the Customer model
        assertEquals(customer, loadedCustomer);
    }
}
