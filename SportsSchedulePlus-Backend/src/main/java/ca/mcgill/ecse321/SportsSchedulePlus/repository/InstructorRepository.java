package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface InstructorRepository extends CrudRepository<Instructor, Integer> {

    List<Instructor> findInstructorByExperience(String experience);
    List<Instructor> findInstructorBySupervisedCourses(ScheduledCourse scheduledCourse);
    Instructor findInstructorByInstructorSuggestedCourseTypes(CourseType courseType);


}
