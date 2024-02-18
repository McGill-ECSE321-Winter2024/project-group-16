package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;


import java.sql.Date;
import java.sql.Time;
import java.util.List;

@SpringBootTest
public class InstructorRepositoryTests {
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
    }

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
    public void testFindInstructorByScheduledCourse() {
        // Create Instructor
        Instructor instructor = createInstructor();

        // Create Scheduled Course
        ScheduledCourse scheduledCourse = createScheduledCourse();
        scheduledCourseRepository.save(scheduledCourse);

        // Add Scheduled Course to Instructor
        instructor.addSupervisedCourse(scheduledCourse);
        instructorRepository.save(instructor);

        // Read Instructor From Database
        List<Instructor> foundInstructors = instructorRepository.findInstructorBySupervisedCourses(scheduledCourse);

        // Assertions
        assertNotNull(foundInstructors);
        assertEquals(instructor,foundInstructors.get(0));

    }

    @Test
    public void testFindInstructorBySuggestedCourse(){
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

        assertEquals(instructor,foundInstructor);

    }

    // Helper method to create a scheduled course
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

    // Helper method to create an instructor
    private Instructor createInstructor(){
        Instructor instructor = new Instructor(1, "dance, yoga, pilates");
        instructorRepository.save(instructor);
        return instructor;
    }
}
