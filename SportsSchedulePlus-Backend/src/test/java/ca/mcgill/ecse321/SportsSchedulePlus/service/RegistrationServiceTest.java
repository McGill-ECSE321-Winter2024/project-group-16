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
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration.Key;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
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
        int customerId2 = 4;
        Customer customer = new Customer();
        Customer customer2 = new Customer();
        customer.setId(customerId);
        customer2.setId(customerId2);
        
        //Create course
        int courseId = 5;
        int courseId2 = 12;
        ScheduledCourse course = new ScheduledCourse();
        ScheduledCourse course2 = new ScheduledCourse();
        course.setId(courseId);
        course2.setId(courseId2);

        //Create key and registration
        Key key = new Key(customer, course);
        Key key2 = new Key(customer2, course2);

        int confirmationNumber = 1234;
        int confirmationNumber2 = 4321;
        Registration registration = new Registration();
        Registration registration2 = new Registration();

        //Set key and confirmation number, save registrations to registrationRepository
        registration.setKey(key);
        registration2.setKey(key2);
        registration.setConfirmationNumber(confirmationNumber);
        registration2.setConfirmationNumber(confirmationNumber2);

        registrationRepository.save(registration);
        registrationRepository.save(registration2);

        //Call getRegistrationsByCustomer
        List<Registration> registrations = registrationService.getRegistrationsByCustomer(customerId);

        //Assert not null and has correct size
        assertNotNull(registrations);
        assertEquals(registrations.size(), 1);

        //Get registration object
        registration = registrations.get(0);

        //Assert registration not null and has correct attributes
        assertNotNull(registration);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
        assertEquals(registration.getKey(), key);
    }

    @Test
    public void getRegistrationsByCourseTest() {
        //Create Customer
        int customerId = 3;
        int customerId2 = 4;
        Customer customer = new Customer();
        Customer customer2 = new Customer();
        customer.setId(customerId);
        customer2.setId(customerId2);
        
        //Create course
        int courseId = 5;
        int courseId2 = 12;
        ScheduledCourse course = new ScheduledCourse();
        ScheduledCourse course2 = new ScheduledCourse();
        course.setId(courseId);
        course2.setId(courseId2);

        //Create key and registration
        Key key = new Key(customer, course);
        Key key2 = new Key(customer2, course2);

        int confirmationNumber = 1234;
        int confirmationNumber2 = 4321;
        Registration registration = new Registration();
        Registration registration2 = new Registration();

        //Set key and confirmation number, save registrations to registrationRepository
        registration.setKey(key);
        registration2.setKey(key2);
        registration.setConfirmationNumber(confirmationNumber);
        registration2.setConfirmationNumber(confirmationNumber2);

        registrationRepository.save(registration);
        registrationRepository.save(registration2);

        //Call getRegistrationsByCourse
        List<Registration> registrations = registrationService.getRegistrationsByCourse(courseId);

        //Assert not null and has correct size
        assertNotNull(registrations);
        assertEquals(registrations.size(), 1);

        //Get registration object
        registration = registrations.get(0);

        //Assert registration not null and has correct attributes
        assertNotNull(registration);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
        assertEquals(registration.getKey(), key);
    }

  

    @Test 
    public void createRegistrationTest() {
        //Create Customer
        int customerId = 3;
        int customerId2 = 4;
        Customer customer = new Customer();
        Customer customer2 = new Customer();
        customer.setId(customerId);
        customer2.setId(customerId2);
        
        //Create course
        int courseId = 5;
        int courseId2 = 12;
        ScheduledCourse course = new ScheduledCourse();
        ScheduledCourse course2 = new ScheduledCourse();
        course.setId(courseId);
        course2.setId(courseId2);

        //Call createRegistration
        Registration registration = registrationService.createRegistration(customerId, courseId);

        //Create key and find registration in the registrationRepository by the key
        Key key = new Key(customer, course);
        Registration foundRegistration = registrationRepository.findRegistrationByKey(key);

        //Assert both registrations are not null and have the correct values
        assertNotNull(foundRegistration);
        assertNotNull(registration);
        assertEquals(foundRegistration.getConfirmationNumber(), registration.getConfirmationNumber());
        assertEquals(foundRegistration, registration);
    }

    
}



