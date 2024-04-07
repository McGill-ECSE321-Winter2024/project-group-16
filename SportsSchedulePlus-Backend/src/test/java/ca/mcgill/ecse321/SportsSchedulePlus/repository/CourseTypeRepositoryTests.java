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
    public void testFindCourseTypeByName() {
        String name = "Zumba";
        String description = "Dance to great music, with great people, and burn a ton of calories without even realizing it.";
        String image = "https://www.verywellfit.com/thmb/SaUyT2h2ujEDx4zCAU0ilFclWqI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/4688722-GettyImages-950806258-06e1e050ab184f3694fd96017c9a42ee.jpg";
        boolean approvedByOwner = true;
        Float price = (float) 50.99;
        CourseType courseType = new CourseType(name, description, image, approvedByOwner, price);

        courseTypeRepository.save(courseType);

        // Read CourseType from database.
        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByName(name);

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
     * Negative result of searching course types by name.
     */
    @Test
    public void testFindByApprovedByOwnerFalse() {
        // Create CourseType not approved by the owner
        String name = "Zumba";
        String description = "Dance to great music, with great people, and burn a ton of calories without even realizing it.";
        String image = "https://www.verywellfit.com/thmb/SaUyT2h2ujEDx4zCAU0ilFclWqI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/4688722-GettyImages-950806258-06e1e050ab184f3694fd96017c9a42ee.jpg";
        boolean approvedByOwner = false;
        Float price = (float) 50.99;
        CourseType notApprovedCourseType = new CourseType(name, description, image, approvedByOwner, price);
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
     * Test to find all course types with a certain price
     */
    @Test
    public void testFindByPrice() {
        String name = "Zumba";
        String description = "Dance to great music, with great people, and burn a ton of calories without even realizing it.";
        String image = "https://www.verywellfit.com/thmb/SaUyT2h2ujEDx4zCAU0ilFclWqI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/4688722-GettyImages-950806258-06e1e050ab184f3694fd96017c9a42ee.jpg";
        boolean approvedByOwner = true;
        Float price = (float) 50.99;
        CourseType newCourseType = new CourseType(name, description, image, approvedByOwner, price);

        List<CourseType> courseTypes = new ArrayList<>();
        courseTypes.add(newCourseType);
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        List<CourseType> foundCourseTypes = courseTypeRepository.findByPrice(newCourseType.getPrice());

        // Asserts
        assertNotNull(courseTypes);
        assertEquals(1, foundCourseTypes.size());
        assertEquals(newCourseType.getPrice(), courseTypes.get(0).getPrice());
        
    }

    /**
     * Negative test to find all course types with a certain price
     */
    @Test
    public void testFindByPriceNegative() {
     
        float coursePrice = 2700.0f;
        List<CourseType> foundCourseTypes = courseTypeRepository.findByPrice(coursePrice);

        // Asserts
        assertNotNull(foundCourseTypes);
        assertEquals(0, foundCourseTypes.size());     
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
     * Negative result of searching course types by name.
     */
    @Test
    public void testFindCourseTypeByNameNotFound() {
        List<CourseType> courseTypes = createCourseTypes();
        for (CourseType courseType : courseTypes) {
            courseTypeRepository.save(courseType);
        }

        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByName("NE");

        // Asserts
        assertNull(loadedCourseType);
    }

    /**
     * Helper method to create a list of CourseTypes.
     * 
     * @return List of CourseTypes
     */
    private static List<CourseType> createCourseTypes() {
        List<CourseType> courseTypes = new ArrayList<>();

        // Add sample course types
        courseTypes.add(new CourseType("Cardio", "cardio description", "cardio image", true, 25.0f));
        courseTypes.add(new CourseType("Stretching", "stretching description", "stretching image", true, 20.0f));
        courseTypes.add(new CourseType("Strength Training", "strength description", "strength image", true, 30.0f));
        courseTypes.add(new CourseType("Yoga", "yoga description", "yoga image", false, 22.0f)); // Not approved by owner

        return courseTypes;
    }
}
