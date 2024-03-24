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
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
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
    private UserService userService;


    /**
     * Clean up the database after each test.
     */
    @AfterEach
    @BeforeEach
    public void clearDatabase() {
        try {
            userService.deleteUser(userService.getOwner().getId());
        } 
        catch (Exception e) {
          //
        }
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        ownerRepository.deleteAll();
       
    }


    @Test
    public void testCreateAndGetScheduledCourse() {
        if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
            userService.createOwner();
        }
        int courseId = createScheduledCourse("Some location", "2024-04-15", "09:00:00", "10:00:00");

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
            createCourseTypeRequest("Description", false, 12f), CourseTypeRequestDTO.class);

        int courseId = createScheduledCourse("Downtown Gym", "2024-04-15", "09:00:00", "11:00:00");


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
        int scheduledCourseId = createScheduledCourse("Downtown Gym", "2024-04-15", "09:00:00", "11:00:00");

        restTemplate.delete("/scheduledCourses/" + scheduledCourseId);

        ResponseEntity < String > response = restTemplate.getForEntity("/scheduledCourses/" + scheduledCourseId,
            String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private int createScheduledCourse(String location, String date, String startTime, String endTime) {
        ResponseEntity < CourseTypeRequestDTO > courseTypeResponse = restTemplate.postForEntity("/courseTypes",
            createCourseTypeRequest("Yoga", true, 20.0f), CourseTypeRequestDTO.class);

        ScheduledCourseRequestDTO scheduledCourseRequest = new ScheduledCourseRequestDTO();
        scheduledCourseRequest.setLocation(location);
        scheduledCourseRequest.setDate(date);
        scheduledCourseRequest.setStartTime(startTime);
        scheduledCourseRequest.setEndTime(endTime);
        scheduledCourseRequest.setCourseType(courseTypeResponse.getBody());

        ResponseEntity < ScheduledCourseRequestDTO > scheduledCourseResponse = restTemplate.postForEntity(
            "/scheduledCourses", scheduledCourseRequest, ScheduledCourseRequestDTO.class);

        return scheduledCourseResponse.getBody().getId();
    }

    private CourseTypeRequestDTO createCourseTypeRequest(String description, boolean approvedByOwner, float price) {
        CourseTypeRequestDTO courseTypeRequest = new CourseTypeRequestDTO();
        courseTypeRequest.setDescription(description);
        courseTypeRequest.setApprovedByOwner(approvedByOwner);
        courseTypeRequest.setPrice(price);
        return courseTypeRequest;
    }
}