/**
 * This class contains unit tests for the PaymentRepository.
 * The overridden equals method in the Payment model is used for assertions.
 */
package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

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
 * Springboot tests for the PaymentRepository class.
 */
@SpringBootTest
public class PaymentRepositoryTests {

    @Autowired
    private PaymentRepository paymentRepository;

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
        paymentRepository.deleteAll();
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
        Payment newPayment = createPayment();
        paymentRepository.save(newPayment);

        // Find payments by confirmation number
        List<Payment> foundPayments = paymentRepository.findPaymentsByConfirmationNumber(newPayment.getConfirmationNumber());
        
        // Assertions
        assertNotNull(foundPayments);
        Payment foundPayment = foundPayments.get(0);
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundPayment);
    }

    /**
     * Tests finding payments by key customer.
     */
    @Test
    public void testFindPaymentsByKeyCustomer() {
        // Create and save a payment
        Payment newPayment = createPayment();
        paymentRepository.save(newPayment);

        // Find payments by key customer
        List<Payment> foundPayments = paymentRepository.findPaymentsByKeyCustomer(newPayment.getKey().getCustomer());

        // Assertions
        assertNotNull(foundPayments);
        Payment foundPayment = foundPayments.get(0);
        
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundPayment);
    }

    /**
     * Tests finding payments by key scheduled course.
     */
    @Test
    public void testFindPaymentsByKeyScheduledCourse() {
        // Create and save a payment
        Payment newPayment = createPayment();
        paymentRepository.save(newPayment);

        // Find payments by key scheduled course
        List<Payment> foundPayments = paymentRepository.findPaymentsByKeyScheduledCourse(newPayment.getKey().getScheduledCourse());

        // Assertions
        assertNotNull(foundPayments);
        Payment foundPayment = foundPayments.get(0);
        
        // The overridden equals method in the Payment model is used here
        assertEquals(newPayment, foundPayment);
    }

    /**
     * Test finding no payments for a customer with no payments.
     */
    @Test
    public void testFindNoPaymentsForCustomerWithNoPayments() {
        // Create and save a customer without payments
        Customer customerWithNoPayments = new Customer();
        customerRepository.save(customerWithNoPayments);

        // Find payments for a customer with no payments
        List<Payment> foundPayments = paymentRepository.findPaymentsByKeyCustomer(customerWithNoPayments);

        // Assert that the list is empty
        assertNotNull(foundPayments);
        assertTrue(foundPayments.isEmpty());
    }

    /**
     * Test finding no payments for a scheduled course with no payments.
     */
    @Test
    public void testFindNoPaymentsForScheduledCourseWithNoPayments() {
        // Create and save a scheduled course without payments
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse scheduledCourseWithNoPayments = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(scheduledCourseWithNoPayments);

        // Find payments for a scheduled course with no payments
        List<Payment> foundPayments = paymentRepository.findPaymentsByKeyScheduledCourse(scheduledCourseWithNoPayments);

        // Assert that the list is empty
        assertNotNull(foundPayments);
        assertTrue(foundPayments.isEmpty());
    }

    /**
     * Test finding payments by non-existing confirmation number.
     */
    @Test
    public void testFindPaymentsByNonExistingConfirmationNumber() {
        // Create a payment
        Payment newPayment = createPayment();
        paymentRepository.save(newPayment);

        // Try to find payments by a non-existing confirmation number
        List<Payment> foundPayments = paymentRepository.findPaymentsByConfirmationNumber(99999);

        // Assert that the list is empty
        assertNotNull(foundPayments);
        assertTrue(foundPayments.isEmpty());
    }

  

    /**
     * Helper method to create a payment with dummy data.
     */
    private Payment createPayment() {
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(scheduledCourse);

        Payment.Key paymentKey = new Payment.Key(customer, scheduledCourse);
        Payment newPayment = new Payment(paymentKey);

        newPayment.setConfirmationNumber(12345);

        return newPayment;
    }
}

