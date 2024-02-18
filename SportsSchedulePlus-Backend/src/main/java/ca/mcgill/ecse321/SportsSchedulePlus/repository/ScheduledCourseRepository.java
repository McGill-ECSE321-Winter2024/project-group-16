package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Interface for managing data related to ScheduledCourses in the application
 */
public interface ScheduledCourseRepository extends CrudRepository<ScheduledCourse, Integer> {

    // Find scheduled courses by location
    List<ScheduledCourse> findScheduledCourseByLocation(String location);
    
    // Find scheduled courses by date
    List<ScheduledCourse> findScheduledCoursesByDate(Date date);
    
    // Find scheduled courses by course type
    List<ScheduledCourse> findScheduledCoursesByCourseType(CourseType courseType);
    
    // Find scheduled courses by start time
    List<ScheduledCourse> findScheduledCoursesByStartTime(Time startTime);
    
    // Find scheduled courses by end time
    List<ScheduledCourse> findScheduledCoursesByEndTime(Time endTime);
}
