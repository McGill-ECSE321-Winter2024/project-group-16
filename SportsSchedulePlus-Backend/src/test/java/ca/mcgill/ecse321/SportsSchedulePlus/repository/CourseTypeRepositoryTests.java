package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

@SpringBootTest
public class CourseTypeRepositoryTests {
    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @AfterEach
    public void clearDatabase() {
        courseTypeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCourseType() {
        // Create CourseType.
        String description = "Zumba";
        boolean approvedByOwner = true;
        Float price = (float) 50.99;
        CourseType courseType = new CourseType(description, approvedByOwner, price);

        // Save CourseType
        courseTypeRepository.save(courseType);

        // Read CourseType from database.
        CourseType loadedCourseType = courseTypeRepository.findCourseTypeByDescription(description);

        // Assers
        assertNotNull(courseType);
        assertEquals(courseType, loadedCourseType);
    }
}
