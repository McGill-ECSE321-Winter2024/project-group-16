package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
/*
 * Springboot test for the CustomerRepository class.
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
    private PaymentRepository payRepo;

    /**
     * Clear the database after each test to ensure a clean state.
     */
    @AfterEach
    public void clearDatabase() {
        payRepo.deleteAll();
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
     * Helper method to create a ScheduledCourse with dummy data.
     */
    private ScheduledCourse createScheduledCourse(int i) {
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);

        return new ScheduledCourse(
            i,
            Date.valueOf("2024-01-01"),
            Time.valueOf("12:00:00"),
            Time.valueOf("13:00:00"),
            "Test Location",
            courseType
        );
    }

    /**
     * Helper method to create a payment with dummy data.
     */
    private Payment createPayment() {
        ScheduledCourse scheduledCourse = createScheduledCourse(1);
        scheduledCourseRepository.save(scheduledCourse);

        Payment newPayment = new Payment();
        newPayment.setConfirmationNumber(12345);

        return newPayment;
    }

    /**
     * Test saving a customer with associated scheduled courses.
     */
    @Test
    public void testSaveCustomerWithScheduledCourses() {
        Customer customer = new Customer();
        customerRepository.save(customer);

        ScheduledCourse firstCourse = createScheduledCourse(1);
        scheduledCourseRepository.save(firstCourse);
        ScheduledCourse secondCourse = createScheduledCourse(2);
        scheduledCourseRepository.save(secondCourse);

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

        ScheduledCourse firstCourse = createScheduledCourse(1);
        scheduledCourseRepository.save(firstCourse);
        ScheduledCourse secondCourse = createScheduledCourse(2);
        scheduledCourseRepository.save(secondCourse);

        customer.addCoursesRegistered(firstCourse);
        customer.addCoursesRegistered(secondCourse);

        Payment firstPayment = createPayment();
        firstPayment.setKey(new Payment.Key(customer, firstCourse));
        payRepo.save(firstPayment);

        Payment secondPayment = createPayment();
        secondPayment.setKey(new Payment.Key(customer, secondCourse));
        payRepo.save(secondPayment);

        customer.addCustomerPayment(firstPayment);
        customer.addCustomerPayment(secondPayment);

        customerRepository.save(customer);

        Customer loadedCustomer = customerRepository.findCustomerById(customer.getId());

        // Asserts using overridden equals method in the Customer model
        assertEquals(customer, loadedCustomer);
    }
}
