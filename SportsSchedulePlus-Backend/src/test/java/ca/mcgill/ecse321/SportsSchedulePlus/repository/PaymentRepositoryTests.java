package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.utils.HelperMethods;


/**
 * This class contains unit tests for the PaymentRepository.
 * The overridden equals method in the Payment model is used for assertions.
 */
@SpringBootTest
public class PaymentRepositoryTests {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private PersonRoleRepository personRoleRepository;

    /**
     * Clears the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        customerRepository.deleteAll();
        personRoleRepository.deleteAll();
    }

    /**
     * Tests finding payments by confirmation number.
     */
    @Test
    public void testFindPaymentsByConfirmationNumber() {
        // Create and save a payment
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = HelperMethods.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newPayment = HelperMethods.createRegistration(customer,course);
        registrationRepository.save(newPayment);

        // Find payments by confirmation number
        Registration foundPayment = registrationRepository.findRegistrationByConfirmationNumber(newPayment.getConfirmationNumber());
        
        // Assertions
        assertNotNull(foundPayment);
        //Payment foundPayment = foundPayments.get(0);
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundPayment);
    }

    /**
     * Tests finding payments by key customer.
     */
    @Test
    public void testFindPaymentsByKeyCustomer() {
        // Create and save a payment
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("New Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = HelperMethods.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newPayment = HelperMethods.createRegistration(customer,course);
        registrationRepository.save(newPayment);

        // Find payments by key customer
        List<Registration> foundPayments = registrationRepository.findRegistrationsByKeyCustomer(newPayment.getKey().getCustomer());

        // Assertions
        assertNotNull(foundPayments);
        Registration foundPayment = foundPayments.get(0);
        
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundPayment);
    }

    /**
     * Tests finding payments by key scheduled course.
     */
    @Test
    public void testFindPaymentsByKeyScheduledCourse() {
        // Create and save a payment
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = HelperMethods.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newPayment = HelperMethods.createRegistration(customer,course);
        registrationRepository.save(newPayment);

        // Find payments by key scheduled course
        List<Registration> foundPayments = registrationRepository.findRegistrationsByKeyScheduledCourse(newPayment.getKey().getScheduledCourse());

        // Assertions
        assertNotNull(foundPayments);
        Registration foundPayment = foundPayments.get(0);
        
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundPayment);
    }

    /**
     * Tests finding payments by key.
     */
    @Test
    public void testFindPaymentsByKey() {
        // Create and save a payment
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("New Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = HelperMethods.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newPayment = HelperMethods.createRegistration(customer,course);
        registrationRepository.save(newPayment);

        // Find payments by key customer
        Registration foundRegistration = registrationRepository.findRegistrationByKey(newPayment.getKey());
        Registration samefoundRegistration = registrationRepository.findRegistrationByKey(new Registration.Key(customer, course));
        // Assertions
        assertNotNull(foundRegistration);
        assertNotNull(samefoundRegistration);
        
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundRegistration);
        assertEquals(newPayment, samefoundRegistration);
    }

    /**
     * Tests finding no payments for a customer with no payments.
     */
    @Test
    public void testFindNoPaymentsForCustomerWithNoPayments() {
        // Create and save a customer without payments
        Customer customerWithNoPayments = new Customer();
        customerRepository.save(customerWithNoPayments);

        // Find payments for a customer with no payments
        List<Registration> foundPayments = registrationRepository.findRegistrationsByKeyCustomer(customerWithNoPayments);

        // Assert that the list is empty
        assertNotNull(foundPayments);
        assertTrue(foundPayments.isEmpty());
    }

    /**
     * Tests finding no payments for a scheduled course with no payments.
     */
    @Test
    public void testFindNoPaymentsForScheduledCourseWithNoPayments() {
        // Create and save a scheduled course without payments
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse scheduledCourseWithNoPayments = HelperMethods.createScheduledCourse(courseType);
        scheduledCourseRepository.save(scheduledCourseWithNoPayments);

        // Find payments for a scheduled course with no payments
        List<Registration> foundPayments = registrationRepository.findRegistrationsByKeyScheduledCourse(scheduledCourseWithNoPayments);

        // Assert that the list is empty
        assertNotNull(foundPayments);
        assertTrue(foundPayments.isEmpty());
    }

    /**
     * Tests finding payments by non-existing confirmation number.
     */
    @Test
    public void testFindPaymentsByNonExistingConfirmationNumber() {
        // Try to find payments by a non-existing confirmation number
        Registration foundPayments = registrationRepository.findRegistrationByConfirmationNumber(99999);

        // Assert that the list is empty
        assertNull(foundPayments);
    }

  
}

