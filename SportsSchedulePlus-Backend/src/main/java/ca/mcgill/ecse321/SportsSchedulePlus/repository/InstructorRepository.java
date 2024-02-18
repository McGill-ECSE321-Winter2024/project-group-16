package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Interface for managing data related to Instructors in the application
public interface InstructorRepository extends CrudRepository<Instructor, Integer> {

    // Find instructors based on their experience
    List<Instructor> findInstructorByExperience(String experience);

    // Find instructors based on the courses they have supervised
    List<Instructor> findInstructorBySupervisedCourses(ScheduledCourse scheduledCourse);

    // Find an instructor based on the suggested course types
    Instructor findInstructorByInstructorSuggestedCourseTypes(CourseType courseType);
}
