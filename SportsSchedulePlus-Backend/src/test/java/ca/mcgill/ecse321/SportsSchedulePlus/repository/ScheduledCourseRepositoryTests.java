package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

@SpringBootTest
public class ScheduledCourseRepositoryTests {

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    // Cleanup the database after each test
    @AfterEach
    public void clearDatabase() {
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
    }

    // Test finding ScheduledCourse by location
    @Test
    public void testFindScheduledCourseByLocation() {
        // Create and save a ScheduledCourse
        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        // Find ScheduledCourse by location
        List<ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCourseByLocation(scheduledCourse.getLocation());

        // Retrieve the found ScheduledCourse
        ScheduledCourse foundCourse = foundCourses.get(0);

        // Assert that the created ScheduledCourse matches the found ScheduledCourse
        assertEquals(scheduledCourse, foundCourse);
    }
     
    // Test finding ScheduledCourses by date
    @Test
    public void testFindScheduledCoursesByDate() {
        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        List<ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByDate(scheduledCourse.getDate());

        ScheduledCourse foundCourse = foundCourses.get(0);

        assertEquals(scheduledCourse, foundCourse);
    }
    
    // Test finding ScheduledCourses by course type
    @Test
    public void testFindScheduledCoursesByCourseType() {
        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        List<ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByCourseType(scheduledCourse.getCourseType());

        ScheduledCourse foundCourse = foundCourses.get(0);

        assertEquals(scheduledCourse, foundCourse);
    }
    
    // Test finding ScheduledCourses by start time
    @Test
    public void testFindScheduledCoursesByStartTime() {
        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        List<ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByStartTime(scheduledCourse.getStartTime());

        ScheduledCourse foundCourse = foundCourses.get(0);

        assertEquals(scheduledCourse, foundCourse);
    }

    // Test finding ScheduledCourses by end time
    @Test
    public void testFindScheduledCoursesByEndTime() {
        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        List<ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByEndTime(scheduledCourse.getEndTime());

        ScheduledCourse foundCourse = foundCourses.get(0);

        assertEquals(scheduledCourse, foundCourse);
    }

    // Helper method to create a ScheduledCourse with dummy data
    private ScheduledCourse createScheduledCourse() {
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);

        return new ScheduledCourse(
            1,
            Date.valueOf("2024-01-01"),
            Time.valueOf("12:00:00"),
            Time.valueOf("13:00:00"),
            "Test Location",
            courseType
        );
    }
}