package ca.mcgill.ecse321.SportsSchedulePlus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import ca.mcgill.ecse321.utils.Helper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InstructorIntegrationTests {

    @Autowired
    private PersonRoleRepository personRoleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;


    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
        personRoleRepository.deleteAll();
        customerRepository.deleteAll();
        instructorRepository.deleteAll();
        ownerRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        
    }

    @BeforeEach
    public void setup(){
        if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
           userService.createOwner();
        }
    }

    private PersonDTO postCustomer(String name, String email, String password) {
        CustomerRequestDTO role = new CustomerRequestDTO();
        PersonDTO personDto = new PersonDTO(name, email, password, role);
        // Send a POST request to the /customers endpoint
        ResponseEntity < PersonDTO > responseEntity = restTemplate.postForEntity("/customers", personDto, PersonDTO.class);
        PersonDTO createdCustomer = responseEntity.getBody();
        return createdCustomer;
      }

    private PersonDTO postInstructor(String name,String email, String password, String experience) {

        PersonDTO newCustomer = postCustomer(name, email, password);
        CustomerRequestDTO role = new CustomerRequestDTO();
        Customer customer = customerRepository.findCustomerById(newCustomer.getId());
        customer.setHasApplied(true);
        customerRepository.save(customer);
        restTemplate.put("/customers/" + newCustomer.getEmail() + "/approve", null);
        PersonDTO updatedCustomer = new PersonDTO("John","newemail@gmail.com","paswd124!!AWVC",role );
        return updatedCustomer;
    }

    @Test
    public void testGetAllInstructors() {
        postInstructor("New instructor","instr@gmail.com","pwd123ABCDEE!!!","");
        ResponseEntity<PersonListResponseDTO> responseEntity = restTemplate.getForEntity("/instructors", PersonListResponseDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("instr@gmail.com",responseEntity.getBody().getPersons().get(0).getEmail());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetInstructorByExperience() {
        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        Instructor instructor =  (Instructor) userService.findPersonByEmail("instructor@gmail.com").getPersonRole();
        instructor.setExperience("Yoga");

        instructorRepository.save(instructor);
        ResponseEntity<PersonListResponseDTO> responseEntity = restTemplate.getForEntity("/instructors", PersonListResponseDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("instructor@gmail.com",responseEntity.getBody().getPersons().get(0).getEmail());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetInstructor() {
        PersonDTO person = postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/instructors/instructor@gmail.com", PersonDTO.class);
        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("instructor@gmail.com",responseEntity.getBody().getEmail());
        assertEquals("New instructor",responseEntity.getBody().getName());

    }

    @Test
    public void testGetInstructorsBySupervisedCourse() {
        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        Instructor instructor =  (Instructor) userService.findPersonByEmail("instructor@gmail.com").getPersonRole();
        instructor.setExperience("Yoga");
        CourseType courseType = new CourseType("Yoga", "Yoga description", "yoga.jpg", true,20f);
        courseTypeRepository.save(courseType);
        ScheduledCourse course = new ScheduledCourse(0,Date.valueOf("2024-05-15"),Time.valueOf("10:00:00"),Time.valueOf("12:00:00"),"Gym",courseType);
        scheduledCourseRepository.save(course);
        instructor.addSupervisedCourse(course);

        instructorRepository.save(instructor);

        ResponseEntity<PersonListResponseDTO> responseEntity = restTemplate.getForEntity("/instructors/supervised-course/"+course.getId(), PersonListResponseDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        assertEquals("instructor@gmail.com",responseEntity.getBody().getPersons().get(0).getEmail());
        assertEquals("New instructor",responseEntity.getBody().getPersons().get(0).getName());
    }


     @Test
    public void testSuggestCourseType() {
        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        // Create a new course type request
        CourseTypeRequestDTO courseTypeRequestDTO = new CourseTypeRequestDTO();
        courseTypeRequestDTO.setName("Yoga Basics");
        courseTypeRequestDTO.setDescription("Yoga Basics description");
        courseTypeRequestDTO.setImage("yoga.jpg");
        courseTypeRequestDTO.setPrice(20.0f);

        // Send a POST request to suggest a course type for the instructor
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/instructors/instructor@gmail.com/suggest-course", courseTypeRequestDTO, String.class, "johndoe@example.com");

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course type suggested successfully.", responseEntity.getBody());
    }

    @Test
    public void testGetInstructorsBySuggestedCourse() {
        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        // Create a new course type request
        CourseTypeRequestDTO courseTypeRequestDTO = new CourseTypeRequestDTO();
        courseTypeRequestDTO.setName("Yoga Basics");
        courseTypeRequestDTO.setDescription("Yoga Basics description");
        courseTypeRequestDTO.setImage("yoga.jpg");
        courseTypeRequestDTO.setPrice(20.0f);

        // Send a POST request to suggest a course type for the instructor
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/instructors/instructor@gmail.com/suggest-course", courseTypeRequestDTO, String.class, "johndoe@example.com");
        
        Instructor instructor =  (Instructor) userService.findPersonByEmail("instructor@gmail.com").getPersonRole();
        int suggestedCourseId = instructor.getInstructorSuggestedCourseTypes().get(0).getId();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course type suggested successfully.", responseEntity.getBody());

        
        ResponseEntity<PersonDTO> newResponseEntity = restTemplate.getForEntity("/instructors/suggestedCourses/"+suggestedCourseId, PersonDTO.class);
        
        assertEquals(HttpStatus.OK, newResponseEntity.getStatusCode());
        assertEquals("instructor@gmail.com",newResponseEntity.getBody().getEmail());
        assertEquals("New instructor",newResponseEntity.getBody().getName());

    }

    @Test
    public void testDeleteInstructor() {

        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        Instructor instructor =  (Instructor) userService.findPersonByEmail("instructor@gmail.com").getPersonRole();

        // Send a DELETE request to delete the instructor
        restTemplate.delete("/instructors/"+instructor.getId());
        
        // Verify that the instructor is no longer in the database
        Person found = personRepository.findPersonByEmail("instructor@gmail.com");
        assertNull(found);
    }

    @Test
    public void testUpdateInstructor() {
        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        Instructor instructor =  (Instructor) userService.findPersonByEmail("instructor@gmail.com").getPersonRole();
    
        // Create a JSON object with updated information
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Updated John Doe");
        requestBody.put("email", "updatedjohndoe@example.com");
        requestBody.put("password", "newpassword!11123ABC");
        requestBody.put("experience", "Advanced");

        // Send a PUT request to update the instructor
        restTemplate.put("/instructors/"+instructor.getId(), requestBody);


        // Verify that the instructor information is updated in the database
        Person updatedInstructor = personRepository.findPersonByEmail("updatedjohndoe@example.com");
        assertNotNull(updatedInstructor);
        assertEquals("Updated John Doe", updatedInstructor.getName());
    }

    @Test
    public void testUpdateInstructorWithInvalidPassword() {
        postInstructor("New instructor","instructor@gmail.com","pwd123ABCDEE!!!","");
        Instructor instructor =  (Instructor) userService.findPersonByEmail("instructor@gmail.com").getPersonRole();
    
        // Create a JSON object with updated information including an invalid password
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Updated John Doe");
        requestBody.put("email", "updatedjohndoe@example.com");
        requestBody.put("password", "invalid"); // Invalid password
        requestBody.put("experience", "Advanced");

        // Send a PUT request to update the instructor
        restTemplate.put("/instructors/"+instructor.getId(), requestBody);
      
        // Verify that the instructor information is not updated in the database
        Person updatedInstructor = personRepository.findPersonByEmail("updatedjohndoe@example.com");
        Person oldInstructor = personRepository.findPersonByEmail("instructor@gmail.com");
        assertNull(updatedInstructor);
        assertNotNull(oldInstructor);
    }
 

}