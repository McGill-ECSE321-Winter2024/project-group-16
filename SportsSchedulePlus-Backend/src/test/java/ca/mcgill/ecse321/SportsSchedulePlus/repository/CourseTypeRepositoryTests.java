package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

@SpringBootTest
public class CourseTypeRepositoryTests {
    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @AfterEach
    public void clearDatabase() {
        //scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
    }

    // Test to find CourseType by description/name
    @Test
    public void testFindCourseTypeByDescription() {
        // Create CourseType.
        String description = "Zumba";
        boolean approvedByOwner = true;
        Float price = (float) 50.99;
        CourseType courseType = new CourseType(description, approvedByOwner, price);

        // Save CourseType
        courseTypeRepository.save(courseType);
        

        // Read CourseType from database.
        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByDescription(description);

        // Asserts
        assertNotNull(loadedCourseType);
        assertEquals(courseType, loadedCourseType);
    }

    // Test to find all approved course types by the owner
    @Test
    public void testFindByApprovedByOwnerTrue() {
        // Create CourseTypes
        List<CourseType> courseTypes = createCourseTypes();

        // Save CourseTypes
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        // Filter Approved CourseTypes
        List<CourseType> approvedCourseTypes = courseTypes.stream().filter(CourseType::getApprovedByOwner).collect(Collectors.toList());

        // Read CourseTypes from database
        List<CourseType> LoadedApprovedCourseTypes = courseTypeRepository.findByApprovedByOwnerTrue();

        // Asserts
        assertNotNull(LoadedApprovedCourseTypes);
        assertEquals(approvedCourseTypes, LoadedApprovedCourseTypes);
    }

    // Test to find all course types that costs less than a max price.
    @Test
    public void testFindByPriceLessThan() {
        // Create and save CourseTypes objects
        List<CourseType> courseTypes = createCourseTypes();
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        // Read CourseTypes smaller than maxPrice
        float maxPrice = 25.0f;
        List<CourseType> courseTypesLessThanMax = courseTypeRepository.findByPriceLessThan(maxPrice);

        // Asserts
        assertNotNull(courseTypesLessThanMax);
        assertEquals(2, courseTypesLessThanMax.size());
        for (CourseType courseType : courseTypesLessThanMax) {
            assertTrue(courseType.getPrice() < maxPrice);
        }
    }

    // Negative result of searching course types by description
    @Test
    public void testFindCourseTypeByDescriptionNotFound() {
        // Create and save CourseTypes.
        List <CourseType> courseTypes = createCourseTypes();
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        // Load from database
        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByDescription("NE");

        // Assert
        assertNull(loadedCourseType);
    }

    // Helper Method to create list of CourseTypes
    private static List<CourseType> createCourseTypes() {
        List<CourseType> courseTypes = new ArrayList<>();

        // Add sample course types
        courseTypes.add(new CourseType("Cardio", true, 25.0f));
        courseTypes.add(new CourseType("Stretching", true, 20.0f));
        courseTypes.add(new CourseType("Strength Training", true, 30.0f));
        courseTypes.add(new CourseType("Yoga", false, 22.0f)); // Not approved by owner

        return courseTypes;
    }

    public static ScheduledCourse createScheduledCourse() {
        ScheduledCourse scheduledCourse = new ScheduledCourse();
        
        // Example values
        scheduledCourse.setId(1);
        scheduledCourse.setDate(new Date(12));
        scheduledCourse.setStartTime(new Time(123));
        scheduledCourse.setEndTime(new Time(1234));
        scheduledCourse.setLocation("Sample Location");

        return scheduledCourse;
    }
}
