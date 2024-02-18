/**
 * This class contains unit tests for the OwnerRepository.
 * The overridden equals method in the Owner model is used for assertions.
 */
package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;

import java.sql.Time;
import java.time.LocalTime;
/**
 * Springboot tests for the OwnerRepository class.
 */
@SpringBootTest
public class OwnerRepositoryTests {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
        personRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        dailyScheduleRepository.deleteAll();
    }

    /**
     * Test finding owner by approved and suggested courses.
     */
    @Test
    public void testFindOwnerByApprovedAndSuggestedCourses() {
        // Create Daily Schedule
        DailySchedule dailySchedule = new DailySchedule();
        dailyScheduleRepository.save(dailySchedule);

        // Create Owner Using Daily Schedule
        Owner owner = new Owner(1, dailySchedule);

        // Create Course Type
        CourseType courseType = new CourseType("Yoga", true, 15.99F);
        courseTypeRepository.save(courseType);

        // Add Suggested and Approved Course Type to Owner
        owner.addOwnerSuggestedCourse(courseType);
        owner.addApprovedCourse(courseType);
        ownerRepository.save(owner);

        // Read Owner From Database
        Owner foundOwnerSuggested = ownerRepository.findOwnerByOwnerSuggestedCourses(courseType);
        Owner foundOwnerApproved = ownerRepository.findOwnerByApprovedCourses(courseType);

        // Assertions
        assertEquals(foundOwnerSuggested, owner); // Uses the overriden equals in the Owner model
        assertEquals(foundOwnerApproved, owner); // Uses the overriden equals in the Owner model
    }
    @Test
    public void testFindOwnerByApprovedAndSuggestedCoursesNotFound(){
        // Create Daily Schedule
        DailySchedule dailySchedule = new DailySchedule();
        dailyScheduleRepository.save(dailySchedule);

        // Create Owner Using Daily Schedule
        Owner owner = new Owner(1, dailySchedule);
        ownerRepository.save(owner);

        // Create Course Type
        CourseType courseType = new CourseType("Yoga", true, 15.99F);
        courseTypeRepository.save(courseType);

        // Read Owner From Database
        Owner foundOwnerSuggested = ownerRepository.findOwnerByOwnerSuggestedCourses(courseType);
        Owner foundOwnerApproved = ownerRepository.findOwnerByApprovedCourses(courseType);

        // Assertions
        assertNull(foundOwnerSuggested);
        assertNull(foundOwnerApproved);

    }

    /**
     * Test finding owner by daily schedule.
     */
    @Test
    public void testFindOwnerByDailySchedule() {
        // Create Daily Schedule
        Time open = Time.valueOf(LocalTime.of(8, 0));
        Time close = Time.valueOf(LocalTime.of(17, 0));
        DailySchedule dailySchedule = new DailySchedule();
        dailySchedule.setOpeningTime(open);
        dailySchedule.setClosingTime(close);

        // Save Daily Schedule
        dailyScheduleRepository.save(dailySchedule);

        // Create Owner Using Daily Schedule
        Owner owner = new Owner(1, dailySchedule);
        ownerRepository.save(owner);

        // Read Owner From Database
        Owner foundOwner = ownerRepository.findOwnerByDailySchedule(dailySchedule);

        // Assertions
        assertEquals(foundOwner, owner); // Uses the overriden equals in the Owner model
    }

    @Test
    public void testFindOwnerByDailyScheduleNotFound(){

        // Create Daily Schedule
        Time open = Time.valueOf(LocalTime.of(8, 0));
        Time close = Time.valueOf(LocalTime.of(17, 0));
        DailySchedule dailySchedule = new DailySchedule();
        dailySchedule.setOpeningTime(open);
        dailySchedule.setClosingTime(close);

        //Save Daily Schedule
        dailyScheduleRepository.save(dailySchedule);

        // Create and Save Owner Without Schedule
        Owner owner = new Owner();
        ownerRepository.save(owner);

        // Read Owner From Database
        Owner foundOwner = ownerRepository.findOwnerByDailySchedule(dailySchedule);

        // Assertions
        assertNull(foundOwner);

    }

}
