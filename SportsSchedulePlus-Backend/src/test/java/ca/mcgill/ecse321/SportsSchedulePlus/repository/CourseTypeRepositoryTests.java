package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

/**
 * Spring boot tests for the CourseTypeRepository class.
 */
@SpringBootTest
public class CourseTypeRepositoryTests {

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    /**
     * Clears the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        courseTypeRepository.deleteAll();
    }

    /**
     * Test to find CourseType by description/name.
     */
    @Test
    public void testFindCourseTypeByDescription() {
        String description = "Zumba";
        boolean approvedByOwner = true;
        Float price = (float) 50.99;
        CourseType courseType = new CourseType(description, approvedByOwner, price);

        courseTypeRepository.save(courseType);

        // Read CourseType from database.
        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByDescription(description);

        // Asserts
        assertNotNull(loadedCourseType);
        assertEquals(courseType, loadedCourseType);
    }

    /**
     * Test to find all approved course types by the owner.
     */
    @Test
    public void testFindByApprovedByOwnerTrue() {
        List<CourseType> courseTypes = createCourseTypes();

        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        // Filter Approved CourseTypes
        List<CourseType> approvedCourseTypes = courseTypes.stream().filter(CourseType::getApprovedByOwner)
                .collect(Collectors.toList());

        // Read CourseTypes from database
        List<CourseType> loadedApprovedCourseTypes = courseTypeRepository.findByApprovedByOwnerTrue();

        // Asserts
        assertNotNull(loadedApprovedCourseTypes);
        assertEquals(approvedCourseTypes, loadedApprovedCourseTypes);
    }

    /**
     * Negative result of searching course types by description.
     */
    @Test
    public void testFindByApprovedByOwnerTrueNegative() {
        // Create CourseType not approved by the owner
        CourseType notApprovedCourseType = new CourseType("Pilates", false, 28.0f);
        courseTypeRepository.save(notApprovedCourseType);

        // Read CourseTypes from database
        List<CourseType> loadedApprovedCourseTypes = courseTypeRepository.findByApprovedByOwnerTrue();

        // Asserts
        assertTrue(loadedApprovedCourseTypes.isEmpty());
    }

    /**
     * Test to find all course types that cost less than a max price.
     */
    @Test
    public void testFindByPriceLessThan() {
        List<CourseType> courseTypes = createCourseTypes();
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        float maxPrice = 25.0f;
        List<CourseType> courseTypesLessThanMax = courseTypeRepository.findByPriceLessThan(maxPrice);

        // Asserts
        assertNotNull(courseTypesLessThanMax);
        assertEquals(2, courseTypesLessThanMax.size());
        for (CourseType courseType : courseTypesLessThanMax) {
            assertTrue(courseType.getPrice() < maxPrice);
        }
    }

    /**
     * Negative result of searching course types by max price.
     */
    @Test
    public void testFindByPriceLessThanNegative() {
        List<CourseType> courseTypes = createCourseTypes();
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        float maxPrice = 5.0f;
        List<CourseType> courseTypesGreaterThanOrEqualToMax = courseTypeRepository.findByPriceLessThan(maxPrice);

        // Asserts
        assertTrue(courseTypesGreaterThanOrEqualToMax.isEmpty());
    }

    /**
     * Negative result of searching course types by description.
     */
    @Test
    public void testFindCourseTypeByDescriptionNotFound() {
        List<CourseType> courseTypes = createCourseTypes();
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByDescription("NE");

        // Asserts
        assertNull(loadedCourseType);
    }

    /**
     * HelperMethods Method to create a list of CourseTypes.
     * 
     * @return List of CourseTypes
     */
    private static List<CourseType> createCourseTypes() {
        List<CourseType> courseTypes = new ArrayList<>();

        // Add sample course types
        courseTypes.add(new CourseType("Cardio", true, 25.0f));
        courseTypes.add(new CourseType("Stretching", true, 20.0f));
        courseTypes.add(new CourseType("Strength Training", true, 30.0f));
        courseTypes.add(new CourseType("Yoga", false, 22.0f)); // Not approved by owner

        return courseTypes;
    }
}
