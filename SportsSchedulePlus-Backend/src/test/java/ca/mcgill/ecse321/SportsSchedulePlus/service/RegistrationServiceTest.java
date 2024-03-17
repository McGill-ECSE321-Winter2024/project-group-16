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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.registrationservice.RegistrationService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;

public class RegistrationServiceTest {

    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserService userService;

    @AfterEach
	public void clearDatabase() {
        registrationRepository.deleteAll();
		personRepository.deleteAll();
        customerRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
	}




    @Test
    public void getAllRegistrationsTest() {
        //Create registrations
        int confirmationNumber = 4321;
        int confirmationNumber2 = 1234;
        Registration registration = new Registration(confirmationNumber);
        Registration registration2 = new Registration(confirmationNumber2);

        //Save registrations
        registrationRepository.save(registration);
        registrationRepository.save(registration2);

        //Call getAllRegistrations()
        List<Registration> registrations = registrationService.getAllRegistrations();

        //Assert that registration is not null and the correct length and contains 
        assertNotNull(registrations);
        assertEquals(registrations.size(), 2);

        //Get registration objects
        registration = registrations.get(0);
        registration2 = registrations.get(1);

        //Assert registration objects not null and have correct attributes
        assertNotNull(registration);
        assertNotNull(registration2);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
        assertEquals(registration2.getConfirmationNumber(), confirmationNumber2);
    }

    @Test
    public void getRegistrationByConfirmationNumberTest() {
        //Create registration
        int confirmationNumber = 4321;
        Registration registration = new Registration(confirmationNumber);

        //Save registration
        registrationRepository.save(registration);

        //Call getRegistrationByConfirmationNumber
        registration = registrationService.getRegistrationByConfirmationNumber(confirmationNumber);

        //Assert registration not null and has correct attribute
        assertNotNull(registration);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
    }

    @Test
    public void getRegistrationsByCustomer() {
        //Create Customer
        int customerId = 3;
        Customer customer = new Customer();
        customer.setId(customerId);

        //Create registration
        int confirmationNumber = 4321;
        Registration registration = new Registration(confirmationNumber);

        registrationRepository.save(registration);

        
        //NOT SURE HOW TO SET A CUSTOMERS REGISTRATION



    }

    @Test
    public void getRegistrationsByCourseTest() {

    }

    @Test
    public void sendPaymentConfirmationEmailTest() {

    }

    @Test 
    public void createRegistrationTest() {

    }

    

    // @Test
    // public void getAllPaymentsTest() {
    //     // Create payments.
    //     int confirmationNumber1 = 1234;
    //     int confirmationNumber2 = 4321;
    //     Payment payment1 = new Payment();
    //     Payment payment2 = new Payment();
    //     payment1.setConfirmationNumber(confirmationNumber1);
    //     payment2.setConfirmationNumber(confirmationNumber2);
        
    //     // Save Payments.
    //     paymentRepository.save(payment1);
    //     paymentRepository.save(payment2);

    //     // Call getAllPayments() 
    //     List<Payment> payments = paymentService.getAllPayments();

    //     // Assert that payments is the correct length and get the payment objects
    //     assertEquals(payments.size(), 2);
    //     payment1 = payments.get(0);
    //     payment2 = payments.get(1);

    //     //Assert that payment is not null and has correct attributes.
    //     assertNotNull(payment1);
    //     assertNotNull(payment2);
    //     assertEquals(confirmationNumber1, payment1.getConfirmationNumber());
    //     assertEquals(confirmationNumber2, payment2.getConfirmationNumber());
    // }

    // @Test
    // public void getPaymentByConfirmationNumberTest() {

    //     // Create payments.
    //     int confirmationNumber = 1234;
    //     Payment payment = new Payment();
    //     payment.setConfirmationNumber(confirmationNumber);
        
    //     // Save Payments.
    //     paymentRepository.save(payment);

    //     // Get payment by using getPaymentByConfirmationNumber()
    //     payment = paymentService.getPaymentByConfirmationNumber(confirmationNumber);

    //     // Assert that payment is not null and has correct attributes
    //     assertNotNull(payment);
    //     assertEquals(confirmationNumber, payment.getConfirmationNumber());
    // }

    // @Test
    // public void getPaymentsByCustomerTest() {
        
    //     // Create customer
    //     int customerId = 3;
    //     Customer customer = new Customer();
    //     customer.setId(customerId);

    //     // Create payment
    //     int confirmationNumber = 1234;
    //     Payment payment = new Payment();
    //     payment.setConfirmationNumber(confirmationNumber);

    //     // Save payment and customer to repositories and add customer payment
    //     paymentRepository.save(payment);
    //     customer.addCustomerPayment(payment);
    //     customerRepository.save(customer);

    //     List<Payment> customerPayments = paymentService.getPaymentsByCustomer(customerId);

    //     assertEquals(customerPayments.size(), 1);
    //     payment = customerPayments.get(0);

    //     // Assert not null and same payment
    //     assertNotNull(payment);
    //     assertEquals(payment.getConfirmationNumber(), confirmationNumber);


    // }
}



