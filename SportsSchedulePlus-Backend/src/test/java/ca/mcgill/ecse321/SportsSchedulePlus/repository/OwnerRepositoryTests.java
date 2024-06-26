package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
/**
 * This class contains unit tests for the OwnerRepository.
 * The overridden equals method in the Owner model is used for assertions.
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
    @Autowired
    private PersonRoleRepository personRoleRepository;

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
        personRoleRepository.deleteAll();
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
        List<DailySchedule> dailyScheduleList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            DailySchedule dailySchedule = new DailySchedule();
            dailySchedule.setOpeningTime(Time.valueOf("08:00:00"));
            dailySchedule.setClosingTime(Time.valueOf("22:00:00"));
            dailyScheduleRepository.save(dailySchedule);
            dailyScheduleList.add(dailySchedule);
        }

        // Create Owner Using Daily Schedule
        Owner owner = new Owner(1, dailyScheduleList);

        // Create Course Type
        CourseType courseType = new CourseType("Sample name", "sample description", "sample image",true, 99.99f);
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
    /**
     * Test finding an owner courses they did not approve/suggest.
     */
    @Test
    public void testFindOwnerByApprovedAndSuggestedCoursesNotFound(){
        // Create Daily Schedule
        List<DailySchedule> dailyScheduleList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            DailySchedule dailySchedule = new DailySchedule();
            dailySchedule.setOpeningTime(Time.valueOf("08:00:00"));
            dailySchedule.setClosingTime(Time.valueOf("22:00:00"));
            dailyScheduleRepository.save(dailySchedule);
            dailyScheduleList.add(dailySchedule);
        }


        // Create Owner Using Daily Schedule
        Owner owner = new Owner(1, dailyScheduleList);
        ownerRepository.save(owner);

        // Create Course Type
        CourseType courseType = new CourseType("Sample name", "sample description", "sample image",true, 99.99f);
        courseTypeRepository.save(courseType);

        // Read Owner From Database
        Owner foundOwnerSuggested = ownerRepository.findOwnerByOwnerSuggestedCourses(courseType);
        Owner foundOwnerApproved = ownerRepository.findOwnerByApprovedCourses(courseType);

        // Assertions
        assertNull(foundOwnerSuggested);
        assertNull(foundOwnerApproved);

    }

    
}
