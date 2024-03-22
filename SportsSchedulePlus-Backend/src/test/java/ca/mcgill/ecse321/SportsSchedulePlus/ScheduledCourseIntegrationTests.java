package ca;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ScheduledCourseIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        // Cleanup the database before and after each test
        scheduledCourseRepository.deleteAll();
        
    }

    @Test
    public void testCreateAndGetScheduledCourse() {
        
        int courseTypeId = createCourseType("Yoga", true, 20.0f); 

        ScheduledCourseDto newScheduledCourse = new ScheduledCourseDto();
        newScheduledCourse.setLocation("Downtown Gym");
        newScheduledCourse.setDate(java.sql.Date.valueOf("2024-04-15"));
        newScheduledCourse.setStartTime(java.sql.Time.valueOf("09:00:00"));
        newScheduledCourse.setEndTime(java.sql.Time.valueOf("11:00:00"));

        CourseTypeDto courseType = new CourseTypeDto();
        courseType.setId(courseTypeId);
        newScheduledCourse.setCourseType(courseType);

        
        ResponseEntity<ScheduledCourseDto> createResponse = restTemplate.postForEntity("/scheduledcourses", newScheduledCourse, ScheduledCourseDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody().getId());

        
        ResponseEntity<ScheduledCourseDto> getResponse = restTemplate.getForEntity("/scheduledcourses/" + createResponse.getBody().getId(), ScheduledCourseDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Downtown Gym", getResponse.getBody().getLocation());
    }

    @Test
    public void testUpdateScheduledCourse() {
        int scheduledCourseId = createScheduledCourse("Downtown Gym", "2024-04-15", "09:00:00", "11:00:00"); // Implement this helper method

        ScheduledCourseDto updateInfo = new ScheduledCourseDto();
        updateInfo.setLocation("Uptown Studio");
        updateInfo.setDate(java.sql.Date.valueOf("2024-05-15"));
        updateInfo.setStartTime(java.sql.Time.valueOf("10:00:00"));
        updateInfo.setEndTime(java.sql.Time.valueOf("12:00:00"));
       

        restTemplate.put("/scheduledcourses/" + scheduledCourseId, updateInfo);

        ResponseEntity<ScheduledCourseDto> response = restTemplate.getForEntity("/scheduledcourses/" + scheduledCourseId, ScheduledCourseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Uptown Studio", response.getBody().getLocation());
        assertEquals(java.sql.Date.valueOf("2024-05-15"), response.getBody().getDate());
    }

    @Test
    public void testDeleteScheduledCourse() {
        int scheduledCourseId = createScheduledCourse("Downtown Gym", "2024-04-15", "09:00:00", "11:00:00"); // Implement this helper method

        restTemplate.delete("/scheduledcourses/" + scheduledCourseId);

        ResponseEntity<String> response = restTemplate.getForEntity("/scheduledcourses/" + scheduledCourseId, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    
    private int createCourseType(String description, boolean approvedByOwner, float price) {
        CourseTypeDto courseType = new CourseTypeDto();
        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);

        ResponseEntity<CourseTypeDto> courseTypeResponse = restTemplate.postForEntity("/coursetypes", courseType, CourseTypeDto.class);
        return courseTypeResponse.getBody().getId();
    }

    
    private int createScheduledCourse(String location, String date, String startTime, String endTime) {
        int courseTypeId = createCourseType("Yoga", true, 20.0f); // 

        ScheduledCourseDto newScheduledCourse = new ScheduledCourseDto();
        newScheduledCourse.setLocation(location);
        newScheduledCourse.setDate(java.sql.Date.valueOf(date));
        newScheduledCourse.setStartTime(java.sql.Time.valueOf(startTime));
        newScheduledCourse.setEndTime(java.sql.Time.valueOf(endTime));

        CourseTypeDto courseType = new CourseTypeDto();
        courseType.setId(courseTypeId); // Set the created CourseType's ID
        newScheduledCourse.setCourseType(courseType);

        ResponseEntity<ScheduledCourseDto> scheduledCourseResponse = restTemplate.postForEntity("/scheduledcourses", newScheduledCourse, ScheduledCourseDto.class);
        assertNotNull(scheduledCourseResponse.getBody());
        assertTrue(scheduledCourseResponse.getStatusCode() == HttpStatus.CREATED);

        return scheduledCourseResponse.getBody().getId();
    }

    class ScheduledCourseDto {
    private int id;
    private String location;
    private Date date;
    private Time startTime;
    private Time endTime;
    private CourseTypeDto courseType;

   
    public ScheduledCourseDto() {}

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public CourseTypeDto getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeDto courseType) {
        this.courseType = courseType;
    }
}
}


