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
import ca.mcgill.ecse321.utils.Helper;


/**
 * This class contains unit tests for the RegistrationRepository.
 * The overridden equals method in the Registration model is used for assertions.
 */
@SpringBootTest
public class RegistrationRepositoryTests {

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
    
    @Autowired
    private PersonRoleRepository personRepository;

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
        personRepository.deleteAll();
    }

    /**
     * Tests finding registrations by confirmation number.
     */
    @Test
    public void testFindRegistrationsByConfirmationNumber() {
        // Create and save a registration
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newRegistration = Helper.createRegistration(customer,course);
        registrationRepository.save(newRegistration);

        // Find registrations by confirmation number
        Registration foundRegistration = registrationRepository.findRegistrationByConfirmationNumber(newRegistration.getConfirmationNumber());
        
        // Assertions
        assertNotNull(foundRegistration);

        // The overridden equals method in the Payment model is used here
        assertEquals(newRegistration, foundRegistration);
    }

    /**
     * Tests finding registrations by key customer.
     */
    @Test
    public void testFindRegistrationsByKeyCustomer() {
        // Create and save a registration
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("New Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newRegistration = Helper.createRegistration(customer,course);
        registrationRepository.save(newRegistration);

        // Find registrations by key customer
        List<Registration> foundRegistrations = registrationRepository.findRegistrationsByKeyCustomer(newRegistration.getKey().getCustomer());

        // Assertions
        assertNotNull(foundRegistrations);
        Registration foundRegistration = foundRegistrations.get(0);
        
        // The overridden equals method in the Registration model is used here
        assertEquals(newRegistration, foundRegistration);
    }

    /**
     * Tests finding registrations by key scheduled course.
     */
    @Test
    public void testFindRegistrationsByKeyScheduledCourse() {
        // Create and save a registration
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newRegistration = Helper.createRegistration(customer,course);
        registrationRepository.save(newRegistration);

        // Find registrations by key scheduled course
        List<Registration> foundRegistrations= registrationRepository.findRegistrationsByKeyScheduledCourse(newRegistration.getKey().getScheduledCourse());

        // Assertions
        assertNotNull(foundRegistrations);
        Registration foundRegistration = foundRegistrations.get(0);
        
        // The overridden equals method in the Registration model is used here
        assertEquals(newRegistration, foundRegistration);
    }

    /**
     * Tests finding registrations by key.
     */
    @Test
    public void testFindRegistrationsByKey() {
        // Create and save a registration
        Customer customer = new Customer();
        customerRepository.save(customer);
        CourseType courseType = new CourseType("New Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(course);
        Registration newRegistration = Helper.createRegistration(customer,course);
        registrationRepository.save(newRegistration);

        // Find registration by key customer
        Registration foundRegistration = registrationRepository.findRegistrationByKey(newRegistration.getKey());
        Registration samefoundRegistration = registrationRepository.findRegistrationByKey(new Registration.Key(customer, course));
        // Assertions
        assertNotNull(foundRegistration);
        assertNotNull(samefoundRegistration);
        
        // The overridden equals method in the Registration model is used here
        assertEquals(newRegistration, foundRegistration);
        assertEquals(newRegistration, samefoundRegistration);
    }

    /**
     * Tests finding no registrations for a customer with no registrations.
     */
    @Test
    public void testFindNoRegistrationsForCustomerWithNoPayments() {
        // Create and save a customer without registrations
        Customer customerWithNoPayments = new Customer();
        customerRepository.save(customerWithNoPayments);

        // Find registrations for a customer with no registrations
        List<Registration> foundRegistrations = registrationRepository.findRegistrationsByKeyCustomer(customerWithNoPayments);

        // Assert that the list is empty
        assertNotNull(foundRegistrations);
        assertTrue(foundRegistrations.isEmpty());
    }

    /**
     * Tests finding no registrations for a scheduled course with no registrations.
     */
    @Test
    public void testFindNoRegistrationsForScheduledCourseWithNoPayments() {
        // Create and save a scheduled course without registrations
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse scheduledCourseWithNoRegistrations = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(scheduledCourseWithNoRegistrations);

        // Find registrations for a scheduled course with no registrations
        List<Registration> foundRegistrations =  registrationRepository.findRegistrationsByKeyScheduledCourse(scheduledCourseWithNoRegistrations);

        // Assert that the list is empty
        assertNotNull(foundRegistrations);
        assertTrue(foundRegistrations.isEmpty());
    }

    /**
     * Tests finding registrations by non-existing confirmation number.
     */
    @Test
    public void testFindRegistrationssByNonExistingConfirmationNumber() {
        // Try to find registrations by a non-existing confirmation number
        Registration foundRegistration = registrationRepository.findRegistrationByConfirmationNumber(99999);

        // Assert that the found registration is empty
        assertNull(foundRegistration);
    }

  
}

