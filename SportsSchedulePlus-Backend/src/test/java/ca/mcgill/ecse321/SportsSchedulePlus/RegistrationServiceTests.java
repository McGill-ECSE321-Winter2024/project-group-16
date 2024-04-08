package ca.mcgill.ecse321.SportsSchedulePlus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.registrationservice.RegistrationService;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTests {

  @Mock
  private RegistrationRepository registrationRepository;
  @Mock
  private CustomerRepository customerRepository;
  @Mock
  private ScheduledCourseRepository scheduledCourseRepository;

  @InjectMocks
  private RegistrationService registrationService;

  @Mock
  private PersonRepository personRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    registrationRepository.deleteAll();
  }

  @Test
  public void testGetAllRegistrations() {
    // Mocking the behavior of RegistrationRepository to return a list of registrations
    when(registrationRepository.findAll()).thenReturn(Arrays.asList(createRegistration(0, 1), createRegistration(2,2)));

    List < Registration > registrations = registrationService.getAllRegistrations();

    assertEquals(2, registrations.size());
  }

  @Test
  public void testDeleteExistingRegistration() {
      // Arrange
      int confirmationNumber = 12345;
      Registration registration = new Registration();
      registration.setConfirmationNumber(confirmationNumber);

      when(registrationRepository.findRegistrationByConfirmationNumber(confirmationNumber)).thenReturn(registration);

      // Act
      boolean result = registrationService.delete(confirmationNumber);

      // Assert
      assertTrue(result);
      verify(registrationRepository, times(1)).delete(registration);
  }
  
  @Test
  public void testGetRegistrationByConfirmationNumber() {
    // Mocking the behavior of RegistrationRepository to return a registration
    int confirmationNumber = 123;
    Registration registration = createRegistration(1, 2);
    registration.setConfirmationNumber(confirmationNumber);

    when(registrationRepository.findRegistrationByConfirmationNumber(confirmationNumber)).thenReturn(registration);

    Registration foundRegistration = registrationService.getRegistrationByConfirmationNumber(123);

    assertNotNull(foundRegistration);
    assertEquals(foundRegistration.getConfirmationNumber(), confirmationNumber);
  }

  @Test
  public void testGetRegistrationsByCustomer() {
    Customer customer = createCustomer(0);
    when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
    when(registrationRepository.findRegistrationsByKeyCustomer(customer)).thenReturn(Arrays.asList(createRegistration(0, 0), createRegistration(0, 1)));

    List < Registration > registrations = registrationService.getRegistrationsByCustomer(1);

    assertEquals(2, registrations.size());
    assertEquals(registrations.get(0).getKey().getCustomer(),customer);
    assertEquals(registrations.get(1).getKey().getCustomer(),customer);
  }

  @Test
  public void testGetRegistrationsByCourse() {
    ScheduledCourse course = createScheduledCourse(0);
    when(scheduledCourseRepository.findById(1)).thenReturn(Optional.of(course));

    when(registrationRepository.findRegistrationsByKeyScheduledCourse(course)).thenReturn(Arrays.asList(createRegistration(1,0), createRegistration(0, 0)));

    List < Registration > registrations = registrationService.getRegistrationsByCourse(1);

    assertEquals(2, registrations.size());
    assertEquals(registrations.get(0).getKey().getScheduledCourse(),course);
    assertEquals(registrations.get(1).getKey().getScheduledCourse(),course);
  }

  @Test
  public void testCreateRegistration() {
    Customer customer = createCustomer(0);
    // Create a mock person
    Person person = new Person();
    person.setName("John Doe");
    person.setEmail("john@example.com");
    person.setPassword("passwor!!AAXD2d");
    person.setPersonRole(customer);
    
    ScheduledCourse course = createScheduledCourse(0);
  
    // Mock the behavior of personRepository.findById to return the person
    when(personRepository.findById(any())).thenReturn(Optional.of(person));
    when(customerRepository.findCustomerById(0)).thenReturn((customer));
    when(scheduledCourseRepository.findById(0)).thenReturn((Optional.of(course)));
    Registration registration = registrationService.createRegistration(0, 0);

    assertNotNull(registration);
    assertEquals(registration.getKey().getCustomer(), customer);
    assertEquals(registration.getKey().getScheduledCourse(), course);
  }

  @Test
  public void testCreateRegistration_CustomerNotFound() {
    when(customerRepository.findCustomerById(1)).thenReturn(null);

    assertThrows(SportsSchedulePlusException.class, () -> {
      registrationService.createRegistration(1, 1);
    });
  }

  @Test
  public void testCreateRegistration_CourseNotFound() {
    when(customerRepository.findCustomerById(1)).thenReturn(new Customer());
    when(scheduledCourseRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(SportsSchedulePlusException.class, () -> {
      registrationService.createRegistration(1, 1);
    });
  }

  @Test
  public void testCreateRegistration_RegistrationExists() {
    Customer customer = new Customer();
    ScheduledCourse course = new ScheduledCourse();
    Registration existingRegistration = new Registration();
    when(customerRepository.findCustomerById(1)).thenReturn(customer);
    when(scheduledCourseRepository.findById(1)).thenReturn(Optional.of(course));
    when(registrationRepository.findRegistrationByKey(any())).thenReturn(existingRegistration);

    assertThrows(SportsSchedulePlusException.class, () -> {
      registrationService.createRegistration(1, 1);
    });
  }

  private Registration createRegistration(int customerId, int courseId) {
    Customer customer = createCustomer(customerId);
    ScheduledCourse course = createScheduledCourse(courseId);
    Registration registration = new Registration();
    registration.setConfirmationNumber(123); // Set confirmation number as needed
    registration.setKey(new Registration.Key(customer, course));
    return registration;
  }

  private Customer createCustomer(int id) {

    Customer customer = new Customer();
    customer.setId(id); // Set customer ID as needed
    // Create a mock person
  
    return customer;
  }

  private ScheduledCourse createScheduledCourse(int id) {
    CourseType courseType = new CourseType("Test Course", "Test course description", "Test course image", false, 50.0f); // Create a course type
    ScheduledCourse scheduledCourse = new ScheduledCourse();
    scheduledCourse.setId(id); // Set course ID as needed
    scheduledCourse.setDate(Date.valueOf("2024-03-30")); // Set date as needed
    scheduledCourse.setStartTime(Time.valueOf("09:00:00")); // Set start time as needed
    scheduledCourse.setEndTime(Time.valueOf("12:00:00")); // Set end time as needed
    scheduledCourse.setLocation("Test Location"); // Set location as needed
    scheduledCourse.setCourseType(courseType); // Assign the course type
    return scheduledCourse;
  }

}