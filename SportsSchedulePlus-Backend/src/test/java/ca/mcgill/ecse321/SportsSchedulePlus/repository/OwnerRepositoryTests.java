package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;

import java.sql.Time;
import java.time.LocalTime;

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

    @AfterEach
    public void clearDatabase(){
        ownerRepository.deleteAll();
        personRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
        dailyScheduleRepository.deleteAll();
    }

    @Test
    public void testFindOwnerByApprovedAndSuggestedCourses(){
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
        assertNotNull(foundOwnerSuggested);
        assertNotNull(foundOwnerApproved);
        assertEquals(foundOwnerSuggested.getId(),owner.getId());
        assertEquals(foundOwnerApproved.getId(), owner.getId());
    }

    @Test
    public void testFindOwnerByDailySchedule(){
        // Create Daily Schedule
        Time open = Time.valueOf(LocalTime.of(8,0));
        Time close = Time.valueOf(LocalTime.of(17,0));

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
        assertNotNull(foundOwner);
        assertEquals(foundOwner.getId(), owner.getId());

    }




    }
