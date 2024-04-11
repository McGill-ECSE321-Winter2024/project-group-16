package ca.mcgill.ecse321.SportsSchedulePlus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.model.State;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;

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
    private CourseTypeRepository courseTypeRepository;

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private DailyScheduleRepository dailyScheduleRepository;
    @Mock
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
    private static final String password = "Password#1";
    private static final String name = "Joe Smith";

    // New user
    private static final String testEmail = "example@email.com";

    // Owner
    private static final String ownerEmail = "sports.schedule.plus@gmail.com";
    private static final String ownerPassword = "admin";
    private static final String ownerName = "owner";

    // Update user info
    private static final String newName = "John Titor";
    private static final String newEmail = "John.Titor@example.com";
    private static final String newPassword = "newPassword#2";
    private static final String newExperience = "5 years";

    @BeforeEach
    public void setMockOutput(){
        MockitoAnnotations.openMocks(this);
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
        person = userService.createCustomer(name, testEmail, password);
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
        person = userService.createOwner();
      
        verify(personRoleRepository, times(1)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));

        assertNotNull(person);
        assertEquals(ownerName, person.getName());
        assertEquals(ownerEmail, person.getEmail());
        assertEquals("admin", person.getPassword());
    }

 
    @Test
    public void testCreateOwnerAlreadyExists() {
        Person person = null;
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(new Owner());

        when(ownerRepository.findAll()).thenReturn(owners);

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
        lenient().when(customerRepository.findCustomerById(person.getId())).thenReturn((customer));
   
        newInstructor = userService.createInstructor(email, "");
      
        assertNotNull(newInstructor);
        assertEquals(person.getEmail(), newInstructor.getEmail());
        assertEquals(person.getName(), newInstructor.getName());
        assertEquals(password, newInstructor.getPassword());
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
        Person person = new Person();
        person.setName(name);
        person.setEmail(email);
        person.setPassword(password);
        person.setPersonRole(customer);

        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);
        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.empty());

        lenient().when(personRepository.findPersonByPersonRole(customer)).thenReturn(person);

        Instructor newInstructor = new Instructor();
        newInstructor.setId(customerIdHasApplied);
        Person instructorPerson = new Person();
        instructorPerson.setName(name);
        instructorPerson.setEmail(email);
        instructorPerson.setPassword(password);
        instructorPerson.setPersonRole(newInstructor);

        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(instructorPerson);
        lenient().when(customerRepository.findById(person.getId())).thenReturn((Optional.of(customer)));
        lenient().when(customerRepository.findCustomerById(person.getId())).thenReturn(((customer)));
        lenient().when(userService.createInstructor(person.getEmail(), "")).thenReturn(instructorPerson);

        Instructor result = null;
        
        result = userService.approveCustomer(person.getEmail());
       
        // Verify
        assertTrue(result instanceof Instructor);
        assertEquals(State.APPROVED, customer.getState());
    }

    @Test
    public void testApproveCustomerNonExistent() {
        Instructor result = null;
        String nonExistingEmail = "nonexistent@gmail.com";
        try {
            result = userService.approveCustomer("nonexistent@gmail.com");
            fail();
        } catch (Exception e) {
            assertNull(result);
            assertEquals("Customer with email " + nonExistingEmail + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void testApproveCustomerHasNotApplied() {
        
        Customer customer = new Customer();
        customer.setId(customerIdHasApplied);
        customer.setHasApplied(false);
        Person person = new Person(name, email, password, customer);

        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);
        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.empty());
        
        Instructor result = null;
        try {
            result = userService.approveCustomer(person.getEmail());
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
        Person person = new Person(name, email, password, instructor);

        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.of(instructor));
        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);
        Instructor result = null;
        try {
            result = userService.approveCustomer(person.getEmail());
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
        Person owner = new Person(ownerName, ownerEmail, ownerPassword, role);
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Owner
        lenient().when(personRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
       
        person = userService.updateUser(ownerId, newName, newEmail, newPassword, newExperience);
       

        assertNotNull(person);
        assertEquals(newName, person.getName());
        assertEquals(ownerEmail, person.getEmail());
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
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Instructor
        Instructor instructorRole = new Instructor();
        instructorRole.setId(customerIdHasApplied);
        instructorRole.setExperience("");
        Person instructor = new Person(name, email, password, instructorRole);
        lenient().when(personRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));

        person = userService.updateUser(customerIdHasApplied, newName, newEmail, newPassword, newExperience);
       
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
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Instructor
        Customer customerRole = new Customer();
        customerRole.setId(customerIdHasApplied);
        Person customer = new Person(name, email, password, customerRole);
        lenient().when(personRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        
        person = userService.updateUser(customerIdHasApplied, newName, newEmail, newPassword, newExperience);
        

        assertNotNull(person);
        assertEquals(newName, person.getName());
        assertEquals(newEmail, person.getEmail());
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
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(role);
        lenient().when(ownerRepository.findAll()).thenReturn(owners);

        // Mock Behavior for finding Instructor
        Customer customerRole = new Customer();
        customerRole.setId(customerIdHasApplied);
        Person customer = new Person(name, email, password, customerRole);
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

    @Test
    public void testApplyForInstructor() {
        int aId = 2;
        String email = "joe@joe.com";
        
        Customer customer = new Customer(aId);
        Person person = new Person("name", email, "password", customer);
        personRepository.save(person);
        customer.setHasApplied(false);
        customerRepository.save(customer);

        lenient().when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        lenient().when(instructorRepository.findById(customer.getId())).thenReturn(Optional.empty());

        customer = userService.applyForInstructor(aId);
        
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
            assertEquals("Customer with ID " + customerId + " does not exist.", e.getMessage());
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

        Customer newCustomer = null;

        try {
            newCustomer = userService.applyForInstructor(aId);
            fail();
        } catch (Exception e) {
            assertNull(newCustomer);
            assertEquals("Customer with ID " + aId + " does not exist.", e.getMessage());
        }

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

        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);

        try {
            newInstructor = userService.approveCustomer(email);
            fail();
        } catch (Exception e) {
            assertNull(newInstructor);
            assertEquals("Customer with ID " + aId + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void testApproveCustomerNullPerson() {
        int aId = 0;
        Person person = new Person();

        Instructor newInstructor = null;
        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);
        try {
            newInstructor = userService.approveCustomer(email);
            fail();
        } catch (Exception e) {
            assertNull(newInstructor);
            
            assertEquals("Customer with ID " + aId + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void testRejectCustomer() {
        int aId = 0;
        Person person = new Person();
       
        Customer customer = new Customer(aId);
        customer.setHasApplied(true);
        person.setPersonRole(customer);
    
        lenient().when(personRepository.findPersonByEmail(email)).thenReturn(person);
        lenient().when(customerRepository.findById(aId)).thenReturn(Optional.of(customer));
        customer = userService.rejectCustomer(email);

        assertNotNull(customer);
        assertEquals(aId, customer.getId());
    }

    @Test
    public void testRejectCustomerNullCustomer() {
        try {
            userService.rejectCustomer(null);
            fail();
        } catch (Exception e) {
            assertEquals("Customer with email " + null + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void getInstructorsBySupervisedCourseTest() {
        int aScheduledCourseId = 2;
        Date aDate = new Date(2345);
        Time aStartTime = new Time(2222);
        Time aEndTime = new Time(2230);
        String email = "joe@joe.com";

        String aCourseName = "a course";
        String aCourseDescription = "a course description";
        String aCourseImage = "a course image";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 2;


        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseName, aCourseDescription, aCourseImage, isApprovedCourse, coursePrice);
        ScheduledCourse scheduledCourse = new ScheduledCourse(aScheduledCourseId, aDate, aStartTime, aEndTime, email, courseType);

        personRepository.save(person);
        instructor.addSupervisedCourse(scheduledCourse);
        instructorRepository.save(instructor);
        List<Instructor> mockInstructors = new ArrayList<>();
        mockInstructors.add(instructor);
        List<Instructor> instructors = null;

        lenient().when(instructorRepository.findInstructorBySupervisedCourses(any(ScheduledCourse.class))).thenReturn(mockInstructors);
      
       
        instructors = userService.getInstructorsBySupervisedCourse(scheduledCourse);  
        assertNotNull(instructors);  
        instructor = instructors.get(0);

        assertNotNull(instructor);
        assertEquals(instructorExperience, instructor.getExperience());
        assertEquals(aId, instructor.getId());
    }

    @Test
    public void getInstructorBySuggestedCourseTypeTest() {
        // Mock data
        Instructor instructor = new Instructor();
        Person person = new Person("Test p", "instructor@example.com", "pwd123q12aW!", instructor);
        CourseType courseType = new CourseType("Test Course", "Test course description", "Test course image", true, 10.0f);

        // Mocking repository behaviors
        when(personRepository.findById(any())).thenReturn(Optional.of(person));
        when(personRepository.findPersonByEmail("instructor@example.com")).thenReturn(person);
        when(instructorRepository.findById(any())).thenReturn(Optional.of(instructor));
        when(courseTypeService.createCourseType("Test Course", "Test course description", "Test course image", true, 10.0f)).thenReturn(courseType);
        when(instructorRepository.findInstructorByInstructorSuggestedCourseTypes(any())).thenReturn(instructor);
       
        // Call method
         userService.suggestCourseType(instructor, courseType);

        Instructor foundInstructor = userService.getInstructorBySuggestedCourseType(courseType.getId());

        assertNotNull(instructor);
      
        assertEquals(foundInstructor, instructor);
    }

    @Test
    public void getInstructorBySuggestedCourseTypeTestNullInstructor() {
        String aCourseName = "a course";
        String aCourseDescription = "a course description";
        String aCourseImage = "a course image";
        boolean isApprovedCourse = true;
        float coursePrice = 2;

        Instructor instructor = null;

        try {
            CourseType courseType = new CourseType(aCourseName, aCourseDescription, aCourseImage, isApprovedCourse, coursePrice);
            int courseTypeId = courseType.getId();
            instructor = userService.getInstructorBySuggestedCourseType(courseTypeId);
            fail();

        } catch (Exception e) {
            assertNull(instructor);
            assertEquals("No Instructor found for the specified CourseType.", e.getMessage());

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
        Instructor foundInstructor = null;

        List<Instructor> mockInstructors = new ArrayList<>();
        mockInstructors.add(instructor);
        List<Instructor> instructors = null;

        when(instructorRepository.findInstructorByExperience(experience)).thenReturn(mockInstructors);
      
        instructors = userService.getInstructorByExperience(experience);
      
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

        try {
            instructors = userService.getInstructorByExperience(experience);
        } catch (Exception e) {
            assertNull(instructors);
            assertEquals("Instructor experience is empty", e.getMessage());
        }
    }



    @Test
    public void testSuggestCourseTypeInstructor() {
        // Mock data
        Instructor instructor = new Instructor();
        Person person = new Person("Test p", "instructor@example.com", "pwd123q12aW!", instructor);
        CourseType courseType = new CourseType("Test Course", "Test course description", "Test course image", true, 10.0f);

        // Mocking repository behaviors
        when(personRepository.findById(any())).thenReturn(Optional.of(person));
        when(personRepository.findPersonByEmail("instructor@example.com")).thenReturn(person);
        when(instructorRepository.findById(any())).thenReturn(Optional.of(instructor));
        when(courseTypeService.createCourseType("Test Course", "Test course description", "Test course image", true, 10.0f)).thenReturn(courseType);

        // Call method
        CourseType suggestedCourseType = userService.suggestCourseType(instructor, courseType);

        // Assert return value
        assertNotNull(suggestedCourseType);
        assertEquals(courseType, suggestedCourseType);
    }

    @Test
    public void testSuggestCourseTypeOwner() {
    // Mock data

    Owner owner = new Owner();

    // Create a list containing the Owner object
    List<Owner> ownerList = new ArrayList<>();
    ownerList.add(owner);

    // Mock the behavior of ownerRepository.findAll()
    when(ownerRepository.findAll()).thenReturn(ownerList);
    CourseType courseType = new CourseType("Test Course", "Test course description", "Test course image", true, 10.0f);
    when(courseTypeService.createCourseType("Test Course", "Test course description", "Test course image", true, 10.0f)).thenReturn(courseType);
    // Call method
     CourseType suggestedCourseType = userService.suggestCourseType(owner, courseType);

    // Assert return value
    assertNotNull(suggestedCourseType);
    assertEquals(courseType, suggestedCourseType);
    }

  
    @Test
    public void testSuggestCourseTypeNullCourseType() {

        String instructorExperience = "12 days";
    
        int aId = 5;

        Instructor instructor = new Instructor(aId, instructorExperience);
   
        CourseType courseType = null;
        CourseType courseTypeCreated = null;
        try {
            courseTypeCreated = userService.suggestCourseType(instructor, courseType);
        } catch (Exception e) {
            assertNull(courseType);
            assertNull(courseTypeCreated);
        }

        verify(courseTypeRepository, times(0)).save(any(CourseType.class));
     
    }

    @Test
    public void testFindPersonByEmail() {
        String aEmail = "email@gmail.com";
        PersonRole customer = new Customer();
        Person person = new Person(name, aEmail, password, customer);
        personRepository.save(person);
        Person foundPerson = null;

        lenient().when(personRepository.findPersonByEmail(aEmail)).thenReturn(person);
        foundPerson = userService.findPersonByEmail(aEmail);
      
        assertNotNull(foundPerson);
        assertEquals(aEmail, foundPerson.getEmail());
        assertEquals(person, foundPerson);
        
        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
        
    }

    @Test
    public void testFindPersonByEmailNullPerson() {
        String aEmail = "email@gmail.com";
        Person person = null;
        Person foundPerson = null;

        lenient().when(personRepository.findPersonByEmail(aEmail)).thenReturn(person);

        try {
            foundPerson = userService.findPersonByEmail(aEmail);
        } catch (Exception e) {
            assertNull(person);
            assertNull(foundPerson);
            assertEquals("No person with email " + aEmail + " found.", e.getMessage());
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(0)).save(any(Person.class));
    }

    @Test
    public void testFindPersonByEmailNullEmail() {
        String aEmail = "";
        Person foundPerson = null;

        try {
            foundPerson = userService.findPersonByEmail(aEmail);
        } catch (Exception e) {
            assertNull(foundPerson);
            assertEquals("Email cannot be null or blank.", e.getMessage());
        }
   
    }
    
    @Test
    public void testGetCourseTypesSuggestedByPersonId() {

        Owner owner = new Owner();
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(owner);

        when(ownerRepository.findAll()).thenReturn(owners);

        String aCourseName = "a course";
        String aCourseDescription = "a course description";
        String aCourseImage = "a course image";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aId = 5;
  
      
        Instructor instructor = new Instructor(aId, instructorExperience);
        Person person = new Person(aName, aEmail, aPassword, instructor);
        CourseType courseType = new CourseType(aCourseName, aCourseDescription, aCourseImage, isApprovedCourse, coursePrice);

        instructor.addInstructorSuggestedCourseType(courseType);
      
        when(personRepository.findById(any())).thenReturn(Optional.of(person));
        when(personRepository.findPersonByEmail(aEmail)).thenReturn(person);
        when(instructorRepository.findById(any())).thenReturn(Optional.of(instructor));

        List<CourseType> courseTypes = null;


        courseTypes = userService.getCourseTypesSuggestedByPersonId(aId);

        assertNotNull(courseTypes);
        courseType = courseTypes.get(0);

        assertNotNull(courseType);
        assertEquals(aCourseName, courseType.getName());
        assertEquals(isApprovedCourse, courseType.getApprovedByOwner());
        assertEquals(coursePrice, courseType.getPrice());
        assertEquals(courseType, courseType);

    

    }

    @Test
    public void testGetCourseTypesSuggestedByPersonIdPersonNotPresent() {
        String aCourseName = "a course";
        String aCourseDescription = "a course description";
        String aCourseImage = "a course image";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String instructorExperience = "12 days";

        int aId = 5;

        Instructor instructor = new Instructor(aId, instructorExperience);
        CourseType courseType = new CourseType(aCourseName, aCourseDescription, aCourseImage, isApprovedCourse, coursePrice);

        instructor.addInstructorSuggestedCourseType(courseType);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aId);
            fail();

        } catch (Exception e) {
            assertNull(courseTypes);
            assertEquals("The owner does not yet exist within the system.", e.getMessage());
        }

    }
    
    @Test
    public void testGetCourseTypesSuggestedByPersonIdOwner() {
        String aCourseName = "a course";
        String aCourseDescription = "a course description";
        String aCourseImage = "a course image";
        boolean isApprovedCourse = true;
        float coursePrice = 2;
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aOwnerId = 5;
     
        Owner owner = new Owner();
        ArrayList<Owner> owners = new ArrayList<>();
        owners.add(owner);

        when(ownerRepository.findAll()).thenReturn(owners);

 
        Person person = new Person(aName, aEmail, aPassword, owner);
        CourseType courseType = new CourseType(aCourseName, aCourseDescription, aCourseImage, isApprovedCourse, coursePrice);

        owner.addOwnerSuggestedCourse(courseType);
        when(personRepository.findById(any())).thenReturn(Optional.of(person));

        List<CourseType> courseTypes = null;

        
        courseTypes = userService.getCourseTypesSuggestedByPersonId(aOwnerId);

    
        assertNotNull(courseTypes);
        courseType = courseTypes.get(0);

        assertNotNull(courseType);
        assertEquals(aCourseName, courseType.getName());
        assertEquals(isApprovedCourse, courseType.getApprovedByOwner());
        assertEquals(coursePrice, courseType.getPrice());
        assertEquals(courseType, courseType);

    

    }

    @Test
    public void testGetCourseTypesSuggestedByPersonIdOwnerNoCourseTypes() {
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aOwnerId = 5;
        
        Owner owner = new Owner();
        Person person = new Person(aName, aEmail, aPassword, owner);
        
        personRepository.save(person);

        List<CourseType> courseTypes = null;

        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aOwnerId);
            fail();
        } catch (Exception e) {
            assertNull(courseTypes);
            assertEquals("The owner does not yet exist within the system.", e.getMessage());
            
        }

        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));

    }

    @Test
    public void testGetCourseTypesSuggestedByPersonIdNotOwnerOrInstructor() {
        String aName = "Jerry";
        String aEmail = "Jerry@joe.com";
        String aPassword = "1234";
        int aOwnerId = 5;

        Customer customer = new Customer(aOwnerId);
        Person person = new Person(aName, aEmail, aPassword, customer);
        
        personRepository.save(person);

        List<CourseType> courseTypes = null;
        try {
            courseTypes = userService.getCourseTypesSuggestedByPersonId(aOwnerId);
            fail();
        } catch (Exception e) {
            assertNull(courseTypes);
            assertEquals("The owner does not yet exist within the system.", e.getMessage());
            
        }
        verify(personRoleRepository, times(0)).save(any(Owner.class));
        verify(personRepository, times(1)).save(any(Person.class));
   

    }



}