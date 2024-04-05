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
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import ca.mcgill.ecse321.utils.Helper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ScheduledCourseIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonRoleRepository personRoleRepository;

    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InstructorRepository instructorRepository;


    @Autowired
    private UserService userService;


    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
        ownerRepository.deleteAll();


        personRoleRepository.deleteAll();
        customerRepository.deleteAll();
        instructorRepository.deleteAll();
        dailyScheduleRepository.deleteAll();

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
        Customer customer = customerRepository.findCustomerById(newCustomer.getId());
        restTemplate.put("/customers/" + customer.getId() + "/apply", null);
        restTemplate.put("/customers/" + newCustomer.getEmail() + "/approve", null);
        ResponseEntity < PersonDTO > getResponse = restTemplate.getForEntity("/instructors/" + newCustomer.getEmail(),PersonDTO.class);
        return getResponse.getBody();
    }


    @Test
    public void testCreateAndGetScheduledCourse() {
        PersonDTO instructorDTO = postInstructor("New instructor","instr@gmail.com","pwd123ABCDEE!!!","");
        int courseId =  Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Some location", "2024-04-15", "09:00:00", "10:00:00", instructorDTO.getId());

        ResponseEntity < ScheduledCourseRequestDTO > getResponse = restTemplate
            .getForEntity("/scheduledCourses/course/" + courseId, ScheduledCourseRequestDTO.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }



    @Test
    public void testUpdateScheduledCourse() {
        ResponseEntity < CourseTypeRequestDTO > courseTypeResponse = restTemplate.postForEntity("/courseTypes",
            Helper.createCourseTypeRequest("Name", "Description", "Image", false, 12f), CourseTypeRequestDTO.class);

        PersonDTO instructorDTO = postInstructor("New instructor","instr@gmail.com","pwd123ABCDEE!!!","");
        int courseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Downtown Gym", "2024-04-15", "09:00:00", "11:00:00", instructorDTO.getId());

        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(courseId).orElse(null);
        assertNotNull(scheduledCourse);

        ScheduledCourseRequestDTO updateInfo = new ScheduledCourseRequestDTO();
        updateInfo.setLocation("Uptown Studio");
        updateInfo.setDate("2024-05-15");
        updateInfo.setStartTime("10:00:00");
        updateInfo.setEndTime("12:00:00");
        updateInfo.setId(courseId);
        updateInfo.setCourseType(courseTypeResponse.getBody());

        restTemplate.put("/scheduledCourses/" + courseId, updateInfo);

        ScheduledCourse updatedScheduledCourse = scheduledCourseRepository.findById(courseId).orElse(null);
        assertNotNull(updatedScheduledCourse);

        ResponseEntity < ScheduledCourseRequestDTO > response = restTemplate
            .getForEntity("/scheduledCourses/course/" + courseId, ScheduledCourseRequestDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateInfo.getLocation(), response.getBody().getLocation());
        assertEquals(updateInfo.getDate(), response.getBody().getDate());
    }

    @Test
    public void testUpdateScheduledCourseWithInvalidInputs() {
    
        restTemplate.postForEntity("/courseTypes",
            Helper.createCourseTypeRequest("Name", "Description", "Image", false, 12f), CourseTypeRequestDTO.class);

        PersonDTO instructorDTO = postInstructor("New instructor","instr@gmail.com","pwd123ABCDEE!!!","");
        int courseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Downtown Gym", "2024-04-15", "09:00:00", "11:00:00", instructorDTO.getId());

        // Attempt to update the scheduled course with invalid inputs
        ScheduledCourseRequestDTO updateInfo = new ScheduledCourseRequestDTO();
        updateInfo.setId(courseId); // Set the ID of the existing scheduled course
        
        restTemplate.put("/scheduledCourses/" + courseId, updateInfo);

        // Asserting that the scheduled course remains unchanged
        ScheduledCourse updatedScheduledCourse = scheduledCourseRepository.findById(courseId).orElse(null);
        assertNotNull(updatedScheduledCourse); // Ensure the scheduled course still exists

        // Check that the scheduled course's location has not changed
        assertEquals("Downtown Gym", updatedScheduledCourse.getLocation());
    }

    @Test
    public void testDeleteScheduledCourse() {
        PersonDTO instructorDTO = postInstructor("New instructor","instr@gmail.com","pwd123ABCDEE!!!","");
        int scheduledCourseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Downtown Gym", "2024-04-15", "09:00:00", "11:00:00", instructorDTO.getId());

        restTemplate.delete("/scheduledCourses/" + scheduledCourseId);

        ResponseEntity < String > response = restTemplate.getForEntity("/scheduledCourses/" + scheduledCourseId,
            String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNonExistingScheduledCourse() {
        // Attempt to delete a scheduled course that doesn't exist
        restTemplate.delete("/scheduledCourses/12345");

        // Verify that the response indicates the scheduled course was not found
        ResponseEntity<String> response = restTemplate.getForEntity("/scheduledCourses/12345", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}