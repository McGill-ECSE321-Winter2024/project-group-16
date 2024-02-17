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

    // Clears the database after each test
    @AfterEach
    public void clearDatabase() {
        paymentRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        customerRepository.deleteAll();
        personRoleRepository.deleteAll();
    }

    // Tests finding payments by confirmation number
    @Test
    public void testFindPaymentsByConfirmationNumber() {
        // Create and save a payment
        Payment newPayment = createPayment();
        paymentRepository.save(newPayment);

        // Find payments by confirmation number
        List<Payment> foundPayments = paymentRepository.findPaymentsByConfirmationNumber(newPayment.getConfirmationNumber());

        // Assertions
        assertNotNull(foundPayments);
        assertTrue(foundPayments.stream().anyMatch(p -> p.getConfirmationNumber() == newPayment.getConfirmationNumber()));
        assertEquals(newPayment, foundPayments.get(0));
    }

    // Tests finding payments by customer
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
        assertEquals(newPayment, foundPayment);
    }

    // Tests finding payments by scheduled course
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
        
        assertEquals(newPayment, foundPayment);
    }

    // Helper method to create a scheduled course with dummy data
    private ScheduledCourse createScheduledCourse() {
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);

        return new ScheduledCourse(1, Date.valueOf("2024-01-01"), Time.valueOf("12:00:00"),
                Time.valueOf("13:00:00"), "Test Location", courseType);
    }

    // Helper method to create a payment with dummy data
    private Payment createPayment() {
        Customer customer = new Customer();
        customerRepository.save(customer);
       

        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        Payment.Key paymentKey = new Payment.Key(customer, scheduledCourse);
        Payment newPayment = new Payment(paymentKey);
        
        newPayment.setConfirmationNumber(12345);

        return newPayment;
    }
}