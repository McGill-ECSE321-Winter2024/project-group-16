package ca.mcgill.ecse321.SportsSchedulePlus.service.userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import java.util.ArrayList;
import java.sql.Time;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
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
    private DailyScheduleRepository dailyScheduleRepository;
    @InjectMocks
    private CourseTypeService courseTypeService;
    @Mock
    private DailyScheduleService dailyScheduleService;

    @InjectMocks
    private UserService userService;

    // Existant user
    private static final Integer customerIdHasApplied = 1;
    //private static final Integer customerIdHasNotApplied = 2;
    private static final Integer ownerId = -1;
    private static final String email = "user@email.com";
    private static final String email2 = "user@hotmail.com";
    private static final String email3 = "user@gmail.com";
    private static final String email4 = "user@yahoo.fr";
    private static final String customerEmail = "customer@email.com";
    private static final String instructorEmail = "instructor@email.com";
    private static final String password = "Password#1";
    private static final String name = "Joe Smith";

    // New user
    private static final String testEmail = "example@email.com";

    // Owners
    private static final String ownerEmail1 = "sports.schedule.plus@gmail.com";
    private static final String ownerEmail2 = "sports.schedule.plus@hotmail.com";
    private static final String ownerEmail3 = "sports.schedule.plus@outlook.fr";
    private static final String ownerEmail4 = "sports.schedule.plus@yahoo.fr";
    private static final String ownerEmail5 = "sports.schedule.plus@bing.com";
    private static final String ownerPassword = "admin";
    private static final String ownerName = "owner";

    // Update user info
    private static final String newName = "John Titor";
    private static final String newEmail = "John.Titor@example.com";
    private static final String newEmail2 = "steve.jobs@apple.com";
    private static final String newPassword = "newPassword#2";
    private static final String newExperience = "5 years";

    private boolean ownerExists = false;

    @BeforeEach
    public void setMockOutput(){
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(personRepository.save(any(Person.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(instructorRepository.save(any(Instructor.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(customerRepository.save(any(Customer.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(ownerRepository.save(any(Owner.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(personRoleRepository.save(any(PersonRole.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(registrationRepository.save(any(Registration.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(dailyScheduleRepository.save(any(DailySchedule.class))).thenAnswer(returnParameterAsAnswer);
    }


    @Test
    public void testCreateCustomer() {
        Person person = null;

        try {
            person = userService.createCustomer(name, testEmail, password);
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        verify(personRoleRepository, times(1)).save(any(Customer.class));
        verify(personRepository, times(1)).save(any(Person.class));

        assertNotNull(person);
        assertEquals(name, person.getName());
        assertEquals(testEmail, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
    }

    @Test
    public void testCreateCustomerBlankName() {
        String testName = "";
        Person person = null;

        try {
            person = userService.createCustomer(testName, testEmail, password);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Name cannot be blank.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Customer.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateCustomerInvalidlEmail() {
        String email = "invalid";
        Person person = null;

        try {
            person = userService.createCustomer(name, email, password);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Email is not valid.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Customer.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateCustomerInvalidlPassword() {
        String InvalidPassword = "invalid";
        Person person = null;

        try {
            person = userService.createCustomer(name, testEmail, InvalidPassword);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Password is not valid.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Customer.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateOwner() {
        Person person = null;

        when(ownerRepository.findAll()).thenReturn(new ArrayList<>());

        try {
            person = userService.createOwner();
        } catch (SportsScheduleException e) {
            // If owner already exists, it will throw an exception and test fails
            fail();
        }

        verify(personRoleRepository, times(1)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));

        assertNotNull(person);
        assertEquals(ownerName, person.getName());
        assertEquals(ownerEmail1, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
    }

    @Test
    public void testCreateOwnerAlreadyExists() {
        ownerExists = true;
        Person person = null;

        when(ownerRepository.findAll()).thenReturn(Collections.singletonList(new Owner()));

        try {
            person = userService.createOwner();
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Owner already exists.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateInstructor() {
        Person person = new Person();
        person.setName(name);
        person.setPassword(password);
        person.setEmail(email);

        Customer customer = new Customer();
        customer.setId(customerIdHasApplied);
        person.setPersonRole(customer);

        Person newInstructor = null;

        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);
        lenient().when(customerRepository.findById(person.getId())).thenReturn(Optional.of(customer));

        try {
            newInstructor = userService.createInstructor(email, "");
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        assertNotNull(newInstructor);
        assertEquals(person.getEmail(), newInstructor.getEmail());
        assertEquals(person.getName(), newInstructor.getName());
        assertEquals("encodedPassword", newInstructor.getPassword());
        assertEquals(Instructor.class, newInstructor.getPersonRole().getClass());
        assertEquals(person.getId(), newInstructor.getId());
    }

    @Test
    public void testCreateInstructor_nonExistentCustomer() {
        String email = "invalid";
        String experience = "5 years";

        Person newInstructor = null;

        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(null);

        try {
            newInstructor = userService.createInstructor(email, experience);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(newInstructor);
            assertEquals("Customer with email " + email + " needs to exist before they can become an Instructor.", e.getMessage());
        }
    }

    @Test
    public void testApproveCustomer() {
        Customer customer = new Customer();
        customer.setId(customerIdHasApplied);
        customer.setHasApplied(true);

        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.empty());

        Person person = new Person();
        person.setName(name);
        person.setEmail(email3);
        person.setPassword(password);
        person.setPersonRole(customer);

        lenient().when(personRepository.findPersonByPersonRole(customer)).thenReturn(person);

        Instructor newInstructor = new Instructor();
        newInstructor.setId(customerIdHasApplied);
        Person instructorPerson = new Person();
        instructorPerson.setName(name);
        instructorPerson.setEmail(email3);
        instructorPerson.setPassword(password);
        instructorPerson.setPersonRole(newInstructor);

        lenient().when(personRepository.findPersonByEmail(person.getEmail())).thenReturn(person);
        lenient().when(customerRepository.findById(person.getId())).thenReturn(Optional.of(customer));
        lenient().when(userService.createInstructor(person.getEmail(), "")).thenReturn(instructorPerson);

        Instructor result = null;
        try {
            result = userService.approveCustomer(customer.getId());
        } catch (Exception e) {
            // Check that no error occurred
            e.printStackTrace();
            fail();
        }

        assertNotNull(result);
        assertTrue(result instanceof Instructor);
        //assertEquals(customerIdHasApplied, result.getId());
    }

    @Test
    public void testApproveCustomerNonExistent() {
        Integer invalidId = 999;
        lenient().when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());
        lenient().when(instructorRepository.findById(invalidId)).thenReturn(Optional.empty());

        Instructor result = null;
        try {
            result = userService.approveCustomer(invalidId);
            fail();
        } catch (Exception e) {
            assertNull(result);
            assertEquals("Customer with ID " + invalidId + " does not exist.", e.getMessage());
        }

    }

    @Test
    public void testApproveCustomerHasNotApplied() {
        Customer customer = new Customer();
        customer.setId(customerIdHasApplied);
        customer.setHasApplied(false);

        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.empty());

        Instructor result = null;
        try {
            result = userService.approveCustomer(customer.getId());
            fail();
        } catch (Exception e) {
            assertNull(result);
            assertEquals("Customer with ID " + customerIdHasApplied + " has not applied to be an instructor.", e.getMessage());
        }
    }

    @Test
    public void testApproveCustomerAlreadyInstructor() {
        Customer customer = new Customer();
        customer.setId(customerIdHasApplied);
        customer.setHasApplied(true);

        Instructor instructor = new Instructor();
        instructor.setId(customerIdHasApplied);

        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.of(instructor));

        Instructor result = null;
        try {
            result = userService.approveCustomer(customer.getId());
            fail();
        } catch (Exception e) {
            assertNull(result);
            assertEquals("Customer with ID " + customerIdHasApplied + " is already an instructor.", e.getMessage());
        }
    }

    @Test
    public void testUpdateUserOwner() {
        Person person = null;

        // Mock Behavior for setting Owner
        Owner role = new Owner();
        role.setId(ownerId);
        role.setDailySchedule(dailyScheduleService.createDailySchedule());
        Person owner = new Person(ownerName, ownerEmail2, ownerPassword, role);
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Owner
        lenient().when(personRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        try {
            person = userService.updateUser(ownerId, newName, newEmail, newPassword, newExperience);
        } catch (Exception e) {
            // Check that no error occurred
            e.printStackTrace();
            fail();
        }

        assertNotNull(person);
        assertEquals(newName, person.getName());
        assertEquals(ownerEmail2, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
        assertEquals(ownerId, person.getId());
    }

    @Test
    public void testUpdateUserInstructor() {
        Person person = null;

        // Mock behavior for setting Owner
        Owner role = new Owner();
        role.setId(ownerId);
        role.setDailySchedule(dailyScheduleService.createDailySchedule());
        Person owner = new Person(ownerName, ownerEmail3, ownerPassword, role);
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Instructor
        Instructor instructorRole = new Instructor();
        instructorRole.setId(customerIdHasApplied);
        instructorRole.setExperience("");
        Person instructor = new Person(name, instructorEmail, password, instructorRole);
        lenient().when(personRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));

        try {
            person = userService.updateUser(customerIdHasApplied, newName, newEmail, newPassword, newExperience);
        } catch (Exception e) {
            // Check that no error occurred
            e.printStackTrace();
            fail();
        }

        assertNotNull(person);
        assertEquals(newName, person.getName());
        assertEquals(newEmail, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
        assertEquals(customerIdHasApplied, person.getId());
        assertEquals(newExperience, ((Instructor) person.getPersonRole()).getExperience());
    }

    @Test
    public void testUpdateUserCustomer() {
        Person person = null;

        // Mock behavior for setting Owner
        Owner role = new Owner();
        role.setId(ownerId);
        role.setDailySchedule(dailyScheduleService.createDailySchedule());
        Person owner = new Person(ownerName, ownerEmail4, ownerPassword, role);
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding customer
        Customer customerRole = new Customer();
        customerRole.setId(customerIdHasApplied);
        Person customer = new Person(name, customerEmail, password, customerRole);
        lenient().when(personRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        try {
            person = userService.updateUser(customerIdHasApplied, newName, newEmail2, newPassword, newExperience);
        } catch (Exception e) {
            // Check that no error occurred
            e.printStackTrace();
            fail();
        }

        assertNotNull(person);
        assertEquals(newName, person.getName());
        assertEquals(newEmail2, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
        assertEquals(customerIdHasApplied, person.getId());
    }

    @Test
    public void testUpdateUserInvalid() {
        Person person = null;
        String invalidEmail = "invalid";

        // Mock behavior for setting Owner
        Owner role = new Owner();
        role.setId(ownerId);
        role.setDailySchedule(dailyScheduleService.createDailySchedule());
        Person owner = new Person(ownerName, ownerEmail5, ownerPassword, role);
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Instructor
        Customer customerRole = new Customer();
        customerRole.setId(customerIdHasApplied);
        Person customer = new Person(name, email2, password, customerRole);
        lenient().when(personRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        try {
            person = userService.updateUser(customerIdHasApplied, newName, invalidEmail, newPassword, newExperience);
            fail();
        } catch (Exception e) {
            // Check that user wasn't updated
            assertNull(person);
            assertEquals("Email is not valid.", e.getMessage());
        }
    }


}

