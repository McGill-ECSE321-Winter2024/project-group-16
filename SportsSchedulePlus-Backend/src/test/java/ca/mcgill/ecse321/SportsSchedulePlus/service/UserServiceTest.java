package ca.mcgill.ecse321.SportsSchedulePlus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

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
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import jakarta.persistence.criteria.CriteriaBuilder.In;

public class UserServiceTest {
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

    // @Test
    // public void getCustomerTest() {

    // }

    // @Test
    // public void getAllCustomersTest() {

    // }

    // @Test
    // public void getInstructorTest() {

    // }

    // @Test
    // public void getAllInstructorsTest () {

    // }

    // @Test
    // public void getOwnerTest() {

    // }

    // @Test
    // public void getPersonByIdTest() {

    // }

    // @Test
    // public void getAllPersonsTest() {

    // }

    // @Test
    // public void deleteUserTest() {

    // }

    @Test
    public void testApplyForInstructor() {
        int aId = 2;
        String email = "joe@joe.com";
        
        Customer customer = new Customer(aId);
        Person person = new Person("name", email, "password", customer);
        personRepository.save(person);
        customer.setHasApplied(false);
        customerRepository.save(customer);

        try {
            customer = userService.applyForInstructor(aId);
        } catch (Exception e) {
            //Ensure no error occured
            fail();
        }
        assertTrue(customer.getHasApplied());

    }

    @Test
    public void testApplyForInstructorIsInstructor() {
        int customerId = 2;
        String aExperience = "2 years";
        
        Instructor instructor = new Instructor(customerId, aExperience);
        Person person = new Person("name", email, "password", instructor);
        personRepository.save(person);
        instructorRepository.save(instructor);

        Customer customer = null;

        try {
            customer = userService.applyForInstructor(customerId);
            fail();
        } catch (Exception e) {
            assertNull(customer);
            assertEquals("Customer with ID " + customerId + " is already an instructor.", e.getMessage());
        }

    }

    @Test
    public void testApplyForInstructorNullCustomer() {
        int customerId = 2;

        Customer customer = null;

        try {
            customer = userService.applyForInstructor(customerId);
            fail();
        } catch (Exception e) {
            assertNull(customer);
            assertEquals("Customer with ID " + customerId + " does not exist.", e.getMessage());
        }

    }

    @Test
    public void testApplyForInstructorAlreadyApplied() {
        int aId = 2;
        String email = "joe@joe.com";
        
        Customer customer = new Customer(aId);
        Person person = new Person("name", email, "password", customer);
        personRepository.save(person);
        customer.setHasApplied(true);
        customerRepository.save(customer);

        try {
            customer = userService.applyForInstructor(customerId);
            fail();
        } catch (Exception e) {
            assertNull(customer);
            assertEquals("Customer with ID " + customerId + " has already applied to be an instructor.", e.getMessage());
        }

    }

    @Test
    public void testApproveCustomer() {
        int aId = 2;
        String email = "joe@joe.com";
        
        Customer customer = new Customer(aId);
        Person person = new Person("name", email, "password", customer);
        personRepository.save(person);
        customer.setHasApplied(true);
        customerRepository.save(customer);
        Instructor instructor = null;

        try {
            instructor = userService.approveCustomer(aId);
        } catch (Exception e) {
            //Ensure no error occurs 
            fail();
        }

        assertNotNull(instructor);
        assertEquals(aId, instructor.getId());

    }

    @Test
    public void testApproveCustomerIsInstructor() {
        int aId = 2;
        String aExperience = "2 years";
        
        Instructor instructor = new Instructor(aId, aExperience);
        Person person = new Person("name", email, "password", instructor);
        personRepository.save(person);
        instructor.setHasApplied(true);
        instructorRepository.save(instructor);

        Instructor newInstructor = null;

        try {
            newInstructor = userService.approveCustomer(aId);
            fail();
        } catch (Exception e) {
            assertNull(newInstructor);
            assertEquals("Customer with ID " + aId + " is already an instructor.", e.getMessage());
        }
    }

    @Test
    public void testApproveCustomerNullPerson() {
        int aId = 2;
        Person person = null;

        Instructor newInstructor = null;

        try {
            newInstructor = userService.approveCustomer(aId);
            fail();
        } catch (Exception e) {
            assertNull(newInstructor);
            assertNull(person);
            assertEquals("Customer with ID " + aId + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void testRejectCustomer() {
        int aId = 2;
        Customer customer = new Customer(aId);
        customer.setHasApplied(true);
        customerRepository.save(customer);

        try {
            customer = userService.rejectCustomer(aId);
        } catch (Exception e) {
            //Ensure no error occurs 
            fail();
        }

        assertNotNull(customer);
        assertEquals(aId, customer.getId());
        assertFalse(customer.getHasApplied());


    }

    @Test
    public void testRejectCustomerNullCustomer() {
        int aId = 2;
        Customer customer = null;

        try {
            customer = userService.rejectCustomer(aId);
            fail();
        } catch (Exception e) {
            assertNull(customer);
            assertEquals("Customer with ID " + aId + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void getInstructorsBySupervisedCourseTest() {
        int aScheduledCourseId = 2;
        Date aDate = new Date(2345);
        Time aStartTime = new Time(2222);
        Time aEndTime = new Time(2230);
        String email = "joe@joe.com";

        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 2;


        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
        ScheduledCourse scheduledCourse = new ScheduledCourse(aScheduledCourseId, aDate, aStartTime, aEndTime, email, courseType);

        personRepository.save(person);
        instructor.addSupervisedCourse(scheduledCourse);
        instructorRepository.save(instructor);
        List<Instructor> instructors = null;

        try {
            instructors = userService.getInstructorsBySupervisedCourse(scheduledCourse);

        } catch (Exception e) {
            //Ensure no error occurs
            fail();
        }      
        assertNotNull(instructors);  
        instructor = instructors.get(0);

        assertNotNull(instructor);
        assertEquals(instructorExperience, instructor.getExperience());
        assertEquals(aId, instructor.getId());
    }

    @Test
    public void getInstructorBySuggestedCourseTypeTest() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;

        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
        instructorRepository.save(instructor);

        instructor.addInstructorSuggestedCourseType(courseType);
        int courseTypeId = courseType.getId();

        try {
            instructor = userService.getInstructorBySuggestedCourseType(courseTypeId);

        } catch (Exception e) {
            fail();

        }

        assertNotNull(instructor);
        assertEquals(instructorExperience, instructor.getExperience());
        assertEquals(aId, instructor.getId());


        verify(instructorRepository, times(1)).save(any(Instructor.class));
        verify(personRepository, times(1)).save(any(Person.class));

    }

    @Test
    public void getInstructorBySuggestedCourseTypeTestNullInstructor() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;

        Instructor instructor = null;

        try {
            CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
            int courseTypeId = courseType.getId();
            instructor = userService.getInstructorBySuggestedCourseType(courseTypeId);
            fail();

        } catch (Exception e) {
            assertNull(instructor);
            assertEquals("No Instructor found for the specific CourseType", e);

        }
    }

    @Test
    public void testGetInstructorByExperience() {
        String email = "email@dog.com";
        String experience = "2 years";
        int aId = 2;
        Instructor instructor = new Instructor(aId, experience);
        Person person = new Person(email, email, email, instructor);

        instructorRepository.save(instructor);
        personRepository.save(person);
        List<Instructor> instructors = null;
        Instructor foundInstructor = null;

        try {
            instructors = userService.getInstructorByExperience(experience);
        } catch (Exception e) {
            fail();
        }
        assertNotNull(instructors);

        foundInstructor = instructors.get(0);
        assertNotNull(foundInstructor);
        assertEquals(experience, foundInstructor.getExperience());
        assertEquals(aId, foundInstructor.getId());

        verify(instructorRepository, times(1)).save(any(Instructor.class));
        verify(personRepository, times(1)).save(any(Person.class));
    

    }

    @Test
    public void testGetInstructorByExperienceNullExperience() {
        String email = "email@dog.com";
        String experience = null;
        int aId = 2;
        Instructor instructor = new Instructor(aId, experience);
        Person person = new Person(email, email, email, instructor);

        instructorRepository.save(instructor);
        personRepository.save(person);
        List<Instructor> instructors = null;
        Instructor foundInstructor = null;

        try {
            instructors = userService.getInstructorByExperience(experience);
            fail();
        } catch (Exception e) {
            assertNull(instructors);
            assertEquals("Instructor experience is empty", e.getMessage());
        }
    }

    @Test
    public void testSuggestCourseTypeInstructor() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;

        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
        CourseType courseTypeCreated = null;
        try {
            courseTypeCreated = userService.suggestCourseType(instructor, courseType);
        } catch (Exception e) {
            //Ensure no error ocured
            fail();
        }
        assertNotNull(courseTypeCreated);
        assertEquals(aCourseDescription, courseTypeCreated.getDescription());
        assertEquals(isApprovedCourse, courseTypeCreated.getApprovedByOwner());
        assertEquals(coursePrice, courseTypeCreated.getPrice());
        assertEquals(courseType, courseTypeCreated);
        assertNotNull(instructor.getInstructorSuggestedCourseTypes());
        assertEquals(courseType, instructor.getInstructorSuggestedCourseType(0));

        verify(instructorRepository, times(1)).save(any(Instructor.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testSuggestCourseTypeOwner() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;
        List <DailySchedule> aDailySchedule;

        Owner owner = new Owner(aId, null);
        Person person = new Person(aName, aEmail, aPassword, owner);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
        CourseType courseTypeCreated = null;

        try {
            courseTypeCreated = userService.suggestCourseType(owner, courseType);
        } catch (Exception e) {
            //Ensure no error ocured
            fail();
        }
        assertNotNull(courseTypeCreated);
        assertEquals(aCourseDescription, courseTypeCreated.getDescription());
        assertEquals(isApprovedCourse, courseTypeCreated.getApprovedByOwner());
        assertEquals(coursePrice, courseTypeCreated.getPrice());
        assertEquals(courseType, courseTypeCreated);
        assertNotNull(owner.getOwnerSuggestedCourses());
        assertEquals(courseType, owner.getOwnerSuggestedCourse(0));

        verify(ownerRepository, times(1)).save(any(Owner.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testSuggestCourseTypeNoPerson() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String aPassword = "1234";
        int aId = 5;

        Instructor instructor = new Instructor(aId, aPassword);
        Person person = null;
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
        CourseType courseTypeCreated = null;

        try {
            courseTypeCreated = userService.suggestCourseType(instructor, courseType);
            fail();
        } catch (Exception e) {
            assertNull(person);
            assertNull(courseTypeCreated);
            assertEquals("Instructor is not associated with any Person object.", e.getMessage());
        }
       
        verify(instructorRepository, times(0)).save(any(Instructor.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testSuggestCourseTypeNullPersonRole() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;

        Instructor instructor = null;
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);
        CourseType courseTypeCreated = null;
        try {
            courseTypeCreated = userService.suggestCourseType(instructor, courseType);
        } catch (Exception e) {
            //Ensure no error occured
            fail();
        }
        assertNotNull(courseTypeCreated);
        assertEquals(aCourseDescription, courseTypeCreated.getDescription());
        assertEquals(isApprovedCourse, courseTypeCreated.getApprovedByOwner());
        assertEquals(coursePrice, courseTypeCreated.getPrice());
        assertEquals(courseType, courseTypeCreated);
        
        verify(instructorRepository, times(0)).save(any(Instructor.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testSuggestCourseTypeNullCourseType() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;

        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = null;
        CourseType courseTypeCreated = null;
        try {
            courseTypeCreated = userService.suggestCourseType(instructor, courseType);
            fail();
        } catch (Exception e) {
            assertNull(courseType);
            assertNull(courseTypeCreated);
            assertEquals("Cannot suggest a null course", e.getMessage());
        }
        
        verify(instructorRepository, times(0)).save(any(Instructor.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testFindPersonByEmail() {
        String email = "email@gmail.com";
        Person person = new Person(name, email, password, null);
        personRepository.save(person);
        Person foundPerson = null;

        try {
            foundPerson = userService.findPersonByEmail(email);
        } catch (Exception e) {
            //Ensure no error occured
            fail();
        }
        assertNotNull(foundPerson);
        assertEquals(email, foundPerson.getEmail());
        assertEquals(person, foundPerson);
        
        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
        
    }

    @Test
    public void testFindPersonByEmailNullPerson() {
        String email = "email@gmail.com";
        Person person = null;
        Person foundPerson = null;

        try {
            foundPerson = userService.findPersonByEmail(email);
            fail();
        } catch (Exception e) {
            assertNull(person);
            assertNull(foundPerson);
            assertEquals("User with email " + email + "does not exist.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testFindPersonByEmailNullEmail() {
        String email = null;
        Person person = new Person();
        personRepository.save(person);
        Person foundPerson = null;

        try {
            foundPerson = userService.findPersonByEmail(email);
            fail();
        } catch (Exception e) {
            assertNull(foundPerson);
            assertEquals("Email " + email + "does not exist for any user.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
    }
    
    @Test
    public void testGetCourseTypesSuggestedByPersonId() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;
        int aOwnerId = 5;
        List <DailySchedule> aDailySchedule;

        Owner owner = new Owner(aOwnerId, null);
        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);

        instructor.addInstructorSuggestedCourseType(courseType);
        personRepository.save(person);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aId);

        } catch (Exception e) {
            fail();
        }

        assertNotNull(courseTypes);
        courseType = courseTypes.get(0);

        assertNotNull(courseType);
        assertEquals(aCourseDescription, courseType.getDescription());
        assertEquals(isApprovedCourse, courseType.getApprovedByOwner());
        assertEquals(coursePrice, courseType.getPrice());
        assertEquals(courseType, courseType);

        verify(personRoleRepository, times(0)).save(any(Instructor.class));
        verify(personRepository, times(1)).save(any(Person.class));
        

    }

    @Test
    public void testGetCourseTypesSuggestedByPersonIdPersonNotPresent() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;
        int aOwnerId = 5;
        List <DailySchedule> aDailySchedule;

        Owner owner = new Owner(aOwnerId, null);
        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);

        instructor.addInstructorSuggestedCourseType(courseType);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aId);
            fail();

        } catch (Exception e) {
            assertNull(courseTypes);
            assertEquals("No person with ID " + aId + " found.", e.getMessage());
        }

    }
    
    @Test
    public void testGetCourseTypesSuggestedByPersonIdOwner() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aOwnerId = 5;
        List <DailySchedule> aDailySchedule = null;

        Owner owner = new Owner(aOwnerId, aDailySchedule);
        Person person = new Person(aName, aEmail, aPassword, owner);
        CourseType courseType = new CourseType(aCourseDescription, isApprovedCourse, coursePrice);

        owner.addOwnerSuggestedCourse(courseType);
        personRepository.save(person);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aOwnerId);

        } catch (Exception e) {
            fail();
        }

        assertNotNull(courseTypes);
        courseType = courseTypes.get(0);

        assertNotNull(courseType);
        assertEquals(aCourseDescription, courseType.getDescription());
        assertEquals(isApprovedCourse, courseType.getApprovedByOwner());
        assertEquals(coursePrice, courseType.getPrice());
        assertEquals(courseType, courseType);

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
   

    }

    @Test
    public void testGetCourseTypesSuggestedByPersonIdOwnerNoCourseTypes() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aOwnerId = 5;
        List <DailySchedule> aDailySchedule = null;

        Owner owner = new Owner(aOwnerId, aDailySchedule);
        Person person = new Person(aName, aEmail, aPassword, owner);
        
        personRepository.save(person);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aOwnerId);
            fail();
        } catch (Exception e) {
            assertNull(courseTypes);
            assertEquals("No course types found for owner.", e.getMessage());
            
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
   

    }

    @Test
    public void testGetCourseTypesSuggestedByPersonIdNotOwnerOrInstructor() {
        String aCourseDescription = "a course";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aOwnerId = 5;
        List <DailySchedule> aDailySchedule = null;

        Customer customer = new Customer(aOwnerId);
        Person person = new Person(aName, aEmail, aPassword, customer);
        
        personRepository.save(person);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aOwnerId);
            fail();
        } catch (Exception e) {
            assertNull(courseTypes);
            assertEquals("Customers cannot have suggested courses.", e.getMessage());
            
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
   

    }

}
