package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.util.Helper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the InstructorRepository.
 * The overridden equals method in the Instructor model is used for assertions.
 */
@SpringBootTest
public class InstructorRepositoryTests {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;

    /**
     * Clean up the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
    }

    /**
     * Test finding instructors by experience.
     */
    @Test
    public void testFindInstructorByExperience() {
        // Create Instructor
        Instructor instructor = createInstructor();

        // Read Instructor From Database
        List<Instructor> foundInstructors = instructorRepository.findInstructorByExperience(instructor.getExperience());

        // Assertions
        assertNotNull(foundInstructors);
        assertEquals(instructor, foundInstructors.get(0));
    }

    @Test
    public void testFindInstructorByExperienceNotFound(){
        // Create Instructor
        Instructor instructor = new Instructor();

        // Read Instructor From Database
        List<Instructor> foundInstructors = instructorRepository.findInstructorByExperience(instructor.getExperience());

        // Assertions
        assertTrue(foundInstructors.isEmpty());
    }

    /**
     * Test finding instructors by supervised course.
     */
    @Test
    public void testFindInstructorByScheduledCourse() {
        // Create Instructor
        Instructor instructor = createInstructor();

        // Create Scheduled Course
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(scheduledCourse);

        // Add Scheduled Course to Instructor
        instructor.addSupervisedCourse(scheduledCourse);
        instructorRepository.save(instructor);

        // Read Instructor From Database
        List<Instructor> foundInstructors = instructorRepository.findInstructorBySupervisedCourses(scheduledCourse);

        // Assertions
        assertNotNull(foundInstructors);
        assertEquals(instructor, foundInstructors.get(0));
    }

    @Test
    public void testFindInstructorBySupervisedCourseNotFound(){
        // Create Scheduled Course
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);
        ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
        scheduledCourseRepository.save(scheduledCourse);

        // Read Instructor From Database
        List<Instructor> foundInstructors = instructorRepository.findInstructorBySupervisedCourses(scheduledCourse);

        // Assertions
        assertTrue(foundInstructors.isEmpty());

    }

    /**
     * Test finding instructors by suggested course.
     */
    @Test
    public void testFindInstructorBySuggestedCourse() {
        // Create Instructor
        Instructor instructor = createInstructor();

        // Create Course Type
        CourseType courseType = new CourseType("pilates", true, 12.99F);
        courseTypeRepository.save(courseType);

        // Add Course Type to Instructor
        instructor.addInstructorSuggestedCourseType(courseType);
        instructorRepository.save(instructor);

        // Read Instructor From Database
        Instructor foundInstructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);

        // Assertions
        assertNotNull(foundInstructor);
        assertEquals(instructor, foundInstructor);
    }

    @Test
    public void testFindInstructorBySuggestedCourseNotFound(){
 
        // Create Course Type
        CourseType courseType = new CourseType("pilates", true, 12.99F);
        courseTypeRepository.save(courseType);

        // Read Instructor From Database
        Instructor foundInstructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);

        // Assertions
        assertNull(foundInstructor);

    }


    /**
     * Helper method to create an instructor.
     */
    private Instructor createInstructor() {
        Instructor instructor = new Instructor(1, "dance, yoga, pilates");
        instructorRepository.save(instructor);
        return instructor;
    }
}
