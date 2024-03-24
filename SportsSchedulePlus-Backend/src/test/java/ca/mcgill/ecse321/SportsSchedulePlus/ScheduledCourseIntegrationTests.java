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
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
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
    private UserService userService;


    /**
     * Clean up the database after each test.
     */
    @AfterEach
    @BeforeEach
    public void clearDatabase() {
       
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        personRepository.deleteAll();
        personRoleRepository.deleteAll();
        ownerRepository.deleteAll(); 
        dailyScheduleRepository.deleteAll();
    }


    @Test
    public void testCreateAndGetScheduledCourse() {
        if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
            userService.createOwner();
        }
        int courseId =  Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Some location", "2024-04-15", "09:00:00", "10:00:00");

        ResponseEntity < ScheduledCourseRequestDTO > getResponse = restTemplate
            .getForEntity("/scheduledCourses/course/" + courseId, ScheduledCourseRequestDTO.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }



    @Test
    public void testUpdateScheduledCourse() {
        if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
            userService.createOwner();
        }

        ResponseEntity < CourseTypeRequestDTO > courseTypeResponse = restTemplate.postForEntity("/courseTypes",
            Helper.createCourseTypeRequest("Description", false, 12f), CourseTypeRequestDTO.class);

        int courseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Downtown Gym", "2024-04-15", "09:00:00", "11:00:00");


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
    public void testDeleteScheduledCourse() {
        int scheduledCourseId = Helper.createScheduledCourse(restTemplate.getRestTemplate(),"Downtown Gym", "2024-04-15", "09:00:00", "11:00:00");

        restTemplate.delete("/scheduledCourses/" + scheduledCourseId);

        ResponseEntity < String > response = restTemplate.getForEntity("/scheduledCourses/" + scheduledCourseId,
            String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}