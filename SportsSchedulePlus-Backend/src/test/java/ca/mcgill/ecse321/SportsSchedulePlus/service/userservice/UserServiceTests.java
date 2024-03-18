package ca.mcgill.ecse321.SportsSchedulePlus.service.userservice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.checkerframework.checker.units.qual.t;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import jakarta.persistence.criteria.CriteriaBuilder.In;

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
    private CourseTypeService courseTypeService;
    @Mock
    private DailyScheduleService dailyScheduleService;

    @InjectMocks
    private UserService userService;

    private static final Integer customerId = 1;
    private static final Integer instructorId = 2;
    private static final Integer ownerId = -1;
    private static final String email = "example@email.com";
    private static final String password = "password";
    private static final String name = "name";

    @BeforeEach
    public void setMockOutput(){
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
        when(personRepository.save(any(Person.class))).thenAnswer(returnParameterAsAnswer);
        when(instructorRepository.save(any(Instructor.class))).thenAnswer(returnParameterAsAnswer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(returnParameterAsAnswer);
        when(ownerRepository.save(any(Owner.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateUser() {
        String name = "Test Name";
        String email = "test@example.com";
        String password = "password";
        PersonRole role = new Customer();
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

       try {
        person = userService.createUser(name, email, password, role);
       } catch (Exception e) {
            // Check that no error occurred
            fail();

        verify(personRoleRepository, times(1)).save(role);
        verify(personRepository, times(1)).save(any(Person.class));

        assertNotNull(person);
        assertEquals(name, person.getName());
        assertEquals(email, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
        assertEquals(role, person.getPersonRole());
        }
    }

    @Test
    public void testCreateUserNullName() {
        String name = null;
        String email = "test@example.com";
        String password = "password";
        PersonRole role = new Customer();
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createUser(name, email, password, role);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Name cannot be empty.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(role);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateUserNullEmail() {
        String name = "Test Name";
        String email = null;
        String password = "password";
        PersonRole role = new Customer();
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createUser(name, email, password, role);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Email cannot be empty.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(role);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateUserNullPassword() {
        String name = "Test Name";
        String email = "test@example.com";
        String password = null;
        PersonRole role = new Customer();
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createUser(name, email, password, role);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Password cannot be empty.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(role);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateUserNullRole() {
        String name = "Test Name";
        String email = "test@example.com";
        String password = "password";
        PersonRole role = null;
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createUser(name, email, password, role);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Role cannot be empty.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(role);
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateCustomer() {
        String name = "Test Name";
        String email = "customer@example.com";
        String password = "password";
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createCustomer(name, email, password);
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        verify(personRoleRepository, times(1)).save(any(Customer.class));
        verify(personRepository, times(1)).save(any(Person.class));

        assertNotNull(person);
        assertEquals(name, person.getName());
        assertEquals(email, person.getEmail());
        assertEquals("encodedPassword", person.getPassword());
    }

    @Test
    public void testCreateCustomerNullName() {
        String name = null;
        String email = "customer@example.com";
        String password = "password";
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createCustomer(name, email, password);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Name cannot be empty.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Customer.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateCustomerNullEmail() {
        String name = "Test Name";
        String email = null;
        String password = "password";
        Person person = null;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        try {
            person = userService.createCustomer(name, email, password);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Email cannot be empty.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Customer.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateOwner() {
        Person person = null;

        when(userService.getOwner()).thenThrow(new RuntimeException());
        when(passwordEncoder.encode(anyString())).thenReturn("admin");

        person = userService.createOwner();

        verify(personRoleRepository, times(1)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));

        assertNotNull(person);
        assertEquals("owner", person.getName());
        assertEquals("sports.schedule.plus@gmail.com", person.getEmail());
        assertEquals("admin", person.getPassword());
    }

    @Test
    public void testCreateOwnerAlreadyExists() {
        Person person = null;

        when(userService.getOwner()).thenReturn(new Owner());

        try {
            person = userService.createOwner();
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNotNull(person);
            assertEquals("Owner already exists.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testCreateInstructor() {
        String email = "instructor@email.com";
        String experience = "5 years";
        Person person = null;

        try {
            person = userService.createInstructor(email, experience);
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        assertNotNull(person);
        assertEquals(email, person.getEmail());
    }

    @Test
    public void testCreateInstructorNonExistentEmail() {
        String email = "invalid";
        String experience = "5 years";
        Person person = null;

        try {
            person = userService.createInstructor(email, experience);
            fail();
        } catch (Exception e) {
            // Check that an error occurred
            assertNull(person);
            assertEquals("Email does not exist.", e.getMessage());
        }
    }

    @Test
    public void testUpdateUser() {
        Integer id = 1;
        String name = "New Name";
        String email = "NewEmail@test.com";
        String password = "newPassword";
        String experience = "5 years";
        Person person = null;

        // Mock behavior for getPersonById
        Person existingUser = new Person("Existing Name", "email@test.com", "password", new Customer());
        when(personRepository.findById(id)).thenReturn(java.util.Optional.of(existingUser));
        try {
            person = userService.updateUser(id, name, email, password, experience);
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        assertNotNull(person);
        assertEquals(name, person.getName());
        assertEquals(email, person.getEmail());
        assertEquals(password, person.getPassword());
    }



}

