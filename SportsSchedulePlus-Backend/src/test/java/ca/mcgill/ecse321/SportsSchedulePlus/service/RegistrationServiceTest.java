package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration.Key;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.registrationservice.RegistrationService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonRoleRepository personRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private CourseTypeService courseTypeService;
    @Mock
    private DailyScheduleService dailyScheduleService;
    @Mock 
    private RegistrationService registrationService;

    @InjectMocks
    private UserService userService;

    private static final Integer confirmationNumber = 3;
    private static final Integer customerId = 1;
    private static final Customer customer = new Customer(customerId);
    private static final Integer instructorId = 2;
    private static final Integer ownerId = -1;
    // private static final Integer courseId = 4;
    private static final ScheduledCourse course = new ScheduledCourse();
    private static final String email = "example@email.com";
    private static final String password = "password";
    private static final String name = "name";
    private static final Key key = new Key(customer, course);

    @BeforeEach
    public void setMockOutput(){

        lenient().when(registrationRepository.findRegistrationByConfirmationNumber(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(confirmationNumber)) {
                Registration registration = new Registration();
                registration.setConfirmationNumber(confirmationNumber);
                return registration;
            } else {
                return null;
            }
        });
// how can i do anyKey?
        lenient().when(registrationRepository.findRegistrationByKey(any())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(key)) {
                Registration registration = new Registration();
                registration.setKey(key);
                return registration;
            } else {
                return null;
            }
        });

        lenient().when(customerRepository.findCustomerById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(customerId)) {
                Customer customer = new Customer();
                customer.setId(customerId);
                return customer;
            } else {
                return null;
            }
        });
        lenient().when(personRepository.findById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(customerId)) {
                Customer customer = new Customer();
                customer.setId(customerId);
                return customer;
            } else {
                return null;
            }
        });
        lenient().when(personRepository.findPersonByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(email)) {
                Person person = new Person();
                person.setEmail(email);
                return person;
            } else {
                return null;
            }
        });
        lenient().when(instructorRepository.findById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(instructorId)) {
                Instructor instructor = new Instructor();
                instructor.setId(instructorId);
                return instructor;
            } else {
                return null;
            }
        });
        lenient().when(ownerRepository.findById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ownerId)) {
                Owner owner = new Owner();
                owner.setId(ownerId);
                return owner;
            } else {
                return null;
            }
        });
        lenient().when(personRepository.findPersonByPersonRole(any(PersonRole.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0) instanceof Customer) {
                Customer customer = invocation.getArgument(0);
                return customer;
            } else if (invocation.getArgument(0) instanceof Instructor) {
                Instructor instructor = (Instructor) invocation.getArgument(0);
                return instructor;
            } else if (invocation.getArgument(0) instanceof Owner) {
                Owner owner = (Owner) invocation.getArgument(0);
                return owner;
            } else {
                return null;
            }
        });
        lenient().when(instructorRepository.findInstructorByInstructorSuggestedCourseTypes(any(CourseType.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0) instanceof CourseType) {
                CourseType courseType = invocation.getArgument(0);
                Instructor instructor = new Instructor();

                return instructor;
            } else {
                return null;
            }
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        when(registrationRepository.save(any(Registration.class))).thenAnswer(returnParameterAsAnswer);
        when(personRepository.save(any(Person.class))).thenAnswer(returnParameterAsAnswer);
        when(instructorRepository.save(any(Instructor.class))).thenAnswer(returnParameterAsAnswer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(returnParameterAsAnswer);
        when(ownerRepository.save(any(Owner.class))).thenAnswer(returnParameterAsAnswer);
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
        assertEquals(2, registrations.size());

        //Get registration objects
        registration = registrations.get(0);
        registration2 = registrations.get(1);

        //Assert registration objects not null and have correct attributes
        assertNotNull(registration);
        assertNotNull(registration2);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
        assertEquals(registration2.getConfirmationNumber(), confirmationNumber2);
        
        verify(registrationRepository, times(2)).save(registration);
        verify(registrationRepository, times(2)).save(any(Registration.class));
    
        
    }

    @Test
    public void testGetRegistrationByConfirmationNumber() {
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
        verify(registrationRepository, times(1)).save(registration);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    
    }
    @Test
    public void testGetRegistrationByConfirmationNumberNullRegistration() {
        //Create registration
        int confirmationNumber = 1234;
        Registration registration = null;

        //Save registration and call getRegistrationByConfirmationNumber
        try {
        registrationRepository.save(registration);
        registration = registrationService.getRegistrationByConfirmationNumber(confirmationNumber);
        fail();
        } catch (Exception e) {
            //Check that error occured
            assertNull(registration);
            assertEquals("There is no payment with confirmation number 1234.", e.getMessage());
        }
        verify(registrationRepository, times(0)).save(registration);
        verify(registrationRepository, times(0)).save(any(Registration.class));
    }

    @Test
    public void testGetRegistrationsByCustomer() {
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
        assertEquals(1, registrations.size());


        //Get registration object
        registration = registrations.get(0);

        //Assert registration not null and has correct attributes
        assertNotNull(registration);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
        assertEquals(registration.getKey(), key);
    }

    @Test
    public void testGetRegistrationsByCustomerNullRegistrations() {
        //Create Customer
        int customerId = 3;
        Customer customer = new Customer();
        customer.setId(customerId);

        //Create key and registration

        Registration registration = null;
        Registration registration2 = null;

        //Call getRegistrationsByCustomer
        try {
        List<Registration> registrations = registrationService.getRegistrationsByCustomer(customerId);
        fail();
        } catch (Exception e) {
            assertNull(registration);
            assertNull(registration2);
            assertEquals("Registration cannot be empty", e.getMessage());

        }
        //Assert not null and has correct size
        verify(registrationRepository, times(0)).save(any(Registration.class));
    }

    @Test
    public void testGetRegistrationsByCustomerNullCustomers() {
        //Create Customer
        int customerId = 3;
        Customer customer = null;
        Customer customer2 = null;
        
        //Create course
        int courseId = 5;
        int courseId2 = 12;
        ScheduledCourse course = new ScheduledCourse();
        ScheduledCourse course2 = new ScheduledCourse();
        course.setId(courseId);
        course2.setId(courseId2);

        //Create registration

        int confirmationNumber = 1234;
        int confirmationNumber2 = 4321;
        Registration registration = new Registration();
        Registration registration2 = new Registration();

        registration.setConfirmationNumber(confirmationNumber);
        registration2.setConfirmationNumber(confirmationNumber2);

        registrationRepository.save(registration);
        registrationRepository.save(registration2);

        //Call getRegistrationsByCustomer
        try {
            List<Registration> registrations = registrationService.getRegistrationsByCustomer(customerId);
            fail();
        } catch (Exception e) {
            assertNull(customer);
            assertNull(customer2);
            assertEquals("There is no customer with ID 1234.", e.getMessage());
        }
        
        
    }

    @Test
    public void testGetRegistrationsByCourseTest() {
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
        assertEquals(1, registrations.size());

        //Get registration object
        registration = registrations.get(0);

        //Assert registration not null and has correct attributes
        assertNotNull(registration);
        assertEquals(registration.getConfirmationNumber(), confirmationNumber);
        assertEquals(registration.getKey(), key);
    }

    @Test
    public void testGetRegistrationsByCourseNullScheduledCourseTest() {
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
        ScheduledCourse course = null;
        ScheduledCourse course2 = null;

        //Create key and registration

        int confirmationNumber = 1234;
        int confirmationNumber2 = 4321;
        Registration registration = new Registration();
        Registration registration2 = new Registration();

        //Set key and confirmation number, save registrations to registrationRepository
        registration.setConfirmationNumber(confirmationNumber);
        registration2.setConfirmationNumber(confirmationNumber2);

        //Call getRegistrationsByCourse
        try {
        List<Registration> registrations = registrationService.getRegistrationsByCourse(courseId);
        fail();
        } catch (Exception e) {
            assertNull(course);
            assertNull(course2);
            assertEquals("There is no scheduled course with  ID " + courseId + ".", e.getMessage());

         }
    }

    @Test 
    public void testCreateRegistration() {
        //Create Customer
        int customerId = 3;
        Customer customer = new Customer();
        customer.setId(customerId);
        
        //Create course
        int courseId = 5;
        ScheduledCourse course = new ScheduledCourse();
        course.setId(courseId);
        
        Registration registration = null;
        //Call createRegistration
        try {
        
        registration = registrationService.createRegistration(customerId, courseId);
        } catch (Exception e) {
            // Check that no error occured
            fail();
        }
        //Create key and find registration in the registrationRepository by the key
        Key key = new Key(customer, course);
        Registration foundRegistration = registrationRepository.findRegistrationByKey(key);
        //Assert both registrations are not null and have the correct values
        assertNotNull(foundRegistration);
        assertNotNull(registration);
        assertEquals(foundRegistration.getConfirmationNumber(), registration.getConfirmationNumber());
        assertEquals(key, foundRegistration.getKey());
        assertEquals(foundRegistration, registration);
        
        verify(registrationRepository, times(1)).save(registration);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    
    }

    @Test 
    public void testCreateRegistrationNullCustomer() {
        //Create Customer
        int customerId = 3;
        Customer customer = null;
        
        //Create course
        int courseId = 5;
        ScheduledCourse course = new ScheduledCourse();
        course.setId(courseId);
        
        //Call createRegistration
        Registration registration = null;
        try {
        registration = registrationService.createRegistration(customerId, courseId);
        fail();
        } catch (Exception e) {
            assertNull(registration);
            assertEquals("There is no customer with ID " + customerId + ".", e.getMessage());
        }
        //Verify no registrations were saved
        verify(registrationRepository, times(0)).save(registration);
        verify(registrationRepository, times(0)).save(any(Registration.class));
    
    }

    @Test 
    public void testCreateRegistrationNullCourse() {
        //Create Customer
        int customerId = 3;
        Customer customer = new Customer();
        customer.setId(customerId);
        
        //Create course
        int courseId = 5;
        ScheduledCourse course = null;
        
        //Call createRegistration
        Registration registration = null;
        try {
        registration = registrationService.createRegistration(customerId, courseId);
        fail();
        } catch (Exception e) {
            assertNull(registration);
            assertEquals("There is no scheduled course with ID " + courseId + ".", e.getMessage());
        }
        //Verify no registrations were saved
        verify(registrationRepository, times(0)).save(registration);
        verify(registrationRepository, times(0)).save(any(Registration.class));
    
    }

    @Test 
    public void testCreateRegistrationPreviousPaymentExists() {
        //Create Customer
        int customerId = 3;
        Customer customer = new Customer();
        customer.setId(customerId);
        
        //Create course
        int courseId = 5;
        ScheduledCourse course = new ScheduledCourse();
        course.setId(courseId);
        
        //Call createRegistration
        Key key = new Key(customer, course);
        Registration previousRegistration = new Registration(key);
        registrationRepository.save(previousRegistration);
        Registration registration = null;
        try {
        registration = registrationService.createRegistration(customerId, courseId);
        fail();
        } catch (Exception e) {
            assertNull(registration);
            assertEquals("The customer with ID " + customerId + " has already paid for the course with ID " + courseId + ".", e.getMessage());
        }
        //Verify no registrations were saved
        verify(registrationRepository, times(1)).save(registration);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    
    }

    

    
}



