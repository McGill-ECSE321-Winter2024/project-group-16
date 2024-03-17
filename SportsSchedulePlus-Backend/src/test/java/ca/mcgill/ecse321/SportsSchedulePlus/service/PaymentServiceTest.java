package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PaymentRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import jakarta.persistence.EmbeddedId;

public class PaymentServiceTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private PaymentService paymentService;

    @AfterEach
	public void clearDatabase() {
        paymentRepository.deleteAll();
		personRepository.deleteAll();
        customerRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
	}

    @Test
    public void getAllPaymentsTest() {
        // Create payments.
        int confirmationNumber1 = 1234;
        int confirmationNumber2 = 4321;
        Payment payment1 = new Payment();
        Payment payment2 = new Payment();
        payment1.setConfirmationNumber(confirmationNumber1);
        payment2.setConfirmationNumber(confirmationNumber2);
        
        // Save Payments.
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        // Call getAllPayments() 
        List<Payment> payments = paymentService.getAllPayments();

        // Assert that payments is the correct length and get the payment objects
        assertEquals(payments.size(), 2);
        payment1 = payments.get(0);
        payment2 = payments.get(1);

        //Assert that payment is not null and has correct attributes.
        assertNotNull(payment1);
        assertNotNull(payment2);
        assertEquals(confirmationNumber1, payment1.getConfirmationNumber());
        assertEquals(confirmationNumber2, payment2.getConfirmationNumber());
    }

    @Test
    public void getPaymentByConfirmationNumberTest() {

        // Create payments.
        int confirmationNumber = 1234;
        Payment payment = new Payment();
        payment.setConfirmationNumber(confirmationNumber);
        
        // Save Payments.
        paymentRepository.save(payment);

        // Get payment by using getPaymentByConfirmationNumber()
        payment = paymentService.getPaymentByConfirmationNumber(confirmationNumber);

        // Assert that payment is not null and has correct attributes
        assertNotNull(payment);
        assertEquals(confirmationNumber, payment.getConfirmationNumber());
    }

    @Test
    public void getPaymentsByCustomerTest() {
        
        // Create customer
        int customerId = 3;
        Customer customer = new Customer();
        customer.setId(customerId);

        // Create payment
        int confirmationNumber = 1234;
        Payment payment = new Payment();
        payment.setConfirmationNumber(confirmationNumber);

        // Save payment and customer to repositories and add customer payment
        paymentRepository.save(payment);
        customer.addCustomerPayment(payment);
        customerRepository.save(customer);

        List<Payment> customerPayments = paymentService.getPaymentsByCustomer(customerId);

        assertEquals(customerPayments.size(), 1);
        payment = customerPayments.get(0);

        // Assert not null and same payment
        assertNotNull(payment);
        assertEquals(payment.getConfirmationNumber(), confirmationNumber);


    }
}



