package ca.mcgill.ecse321.SportsSchedulePlus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.registration.RegistrationListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.registration.RegistrationResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import ca.mcgill.ecse321.utils.Helper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RegistrationIntegrationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ScheduledCourseRepository scheduledCourseRepository;

  @Autowired
  private CourseTypeRepository courseTypeRepository;
  @Autowired
  private OwnerRepository ownerRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PersonRoleRepository personRoleRepository;

  @Autowired
  private PersonRepository personRepository;



  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

  @Autowired
  private DailyScheduleRepository dailyScheduleRepository;

  @BeforeEach
  @AfterEach
  public void clearDatabase() {
    registrationRepository.deleteAll();
    scheduledCourseRepository.deleteAll();
    courseTypeRepository.deleteAll();
   
    personRepository.deleteAll();
    ownerRepository.deleteAll();

    personRoleRepository.deleteAll();
    customerRepository.deleteAll();
    dailyScheduleRepository.deleteAll();

  }

  private PersonDTO postCustomer(String name, String email, String password) {

    CustomerRequestDTO role = new CustomerRequestDTO();

    PersonDTO personDto = new PersonDTO(name, email, password, role);

    // Send a POST request to the /customers endpoint

    ResponseEntity < PersonDTO > responseEntity = restTemplate.postForEntity("/customers", personDto, PersonDTO.class);

    PersonDTO createdCustomer = responseEntity.getBody();

    return createdCustomer;
  }

  
  @Test
  public void testCreateAndGetRegistration() {

    if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
      userService.createOwner();
    }
    int customerId = postCustomer("Test", "xhzwwww@gmail.com", "123abvwwQ!!").getId();
    int courseId = Helper.createScheduledCourse( restTemplate.getRestTemplate(),"Some location", "2024-04-15", "09:00:00", "10:00:00");
    // Create Registration
    ResponseEntity < RegistrationResponseDTO > createResponse = restTemplate.postForEntity(
      "/registrations/" + customerId + "/" + courseId, null, RegistrationResponseDTO.class);
    assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    RegistrationResponseDTO createdRegistration = createResponse.getBody();
    assertNotNull(createdRegistration);
    assertNotNull(createdRegistration.getConfirmationNumber());

    // Get Registration
    ResponseEntity < RegistrationResponseDTO > getResponse = restTemplate.getForEntity(
      "/registrations/" + createdRegistration.getConfirmationNumber(), RegistrationResponseDTO.class);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    RegistrationResponseDTO retrievedRegistration = getResponse.getBody();
    assertNotNull(retrievedRegistration);
    assertEquals(customerId, retrievedRegistration.getCustomer().getId());
    assertEquals(courseId, retrievedRegistration.getScheduledCourse().getId());
  }

  @Test
  public void testGetRegistrationsByCustomer() {

    if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
      userService.createOwner();
    }
    int customerId = postCustomer("Test", "xhzwwwabcdw@gmail.com", "123abvwwQ!!").getId();
    int courseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Some location", "2024-04-15", "09:00:00", "10:00:00");
    // Create Registration
    restTemplate.postForEntity(
      "/registrations/" + customerId + "/" + courseId, null, RegistrationResponseDTO.class);
    // Get Registrations by Customer
    ResponseEntity < RegistrationListResponseDTO > getResponse = restTemplate
      .getForEntity("/customers/" + customerId + "/registrations", RegistrationListResponseDTO.class);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    RegistrationListResponseDTO registrations = getResponse.getBody();
    assertNotNull(registrations);

    assertEquals(registrations.getRegistrations().get(0).getCustomer().getId(),customerId);
   
  }

  @Test
  public void testGetRegistrationsByCourse() {

    if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
      userService.createOwner();
    }
    int customerId = postCustomer("Test", "xhzw222wwabcdw@gmail.com", "123abvwwQ!!").getId();
    int newCustomerId = postCustomer("Test", "abcdwe2wwabcdw@gmail.com", "123abvwwQ!!").getId();
    int courseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Some location", "2024-04-15", "09:00:00", "10:00:00");

    // Create Registration
    restTemplate.postForEntity("/registrations/" + customerId + "/" + courseId, null, RegistrationResponseDTO.class);

    restTemplate.postForEntity("/registrations/" + newCustomerId + "/" + courseId, null, RegistrationResponseDTO.class);

    // Get Registrations by Course
    ResponseEntity < RegistrationListResponseDTO > getResponse = restTemplate
      .getForEntity("/courses/" + courseId + "/registrations", RegistrationListResponseDTO.class);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    RegistrationListResponseDTO registrations = getResponse.getBody();
    assertNotNull(registrations);
    assertEquals(registrations.getRegistrations().get(0).getScheduledCourse().getId(),courseId);
    assertEquals(registrations.getRegistrations().get(1).getScheduledCourse().getId(),courseId);

  }

  @Test
  public void testDeleteRegistration() {
    int courseId = 1;
    int customerId = 0;
    if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
      userService.createOwner();
    }
    customerId = postCustomer("Test", "abcdedw@gmail.com", "123abvwwQ!!").getId();
    courseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Some location", "2024-04-15", "09:00:00", "10:00:00");

    // Create Registration
    ResponseEntity < RegistrationResponseDTO > createResponse = restTemplate.postForEntity(
      "/registrations/" + customerId + "/" + courseId, null, RegistrationResponseDTO.class);
    assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    RegistrationResponseDTO createdRegistration = createResponse.getBody();
    assertNotNull(createdRegistration);
    assertNotNull(createdRegistration.getConfirmationNumber());

    // Delete Registration
    restTemplate.delete("/registrations/" + createdRegistration.getConfirmationNumber());

    // Try to retrieve the deleted registration
    ResponseEntity < RegistrationResponseDTO > getResponse = restTemplate.getForEntity(
      "/registrations/" + createdRegistration.getConfirmationNumber(), RegistrationResponseDTO.class);
    assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
  }

}