package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface for managing data related to Owners in the application
 */
public interface OwnerRepository extends CrudRepository<Owner, Integer> {

    // Find Owner by suggested courses
    Owner findOwnerByOwnerSuggestedCourses(CourseType courseType);

    // Find Owner by approved courses
    Owner findOwnerByApprovedCourses(CourseType courseType);

    // Find Owner by daily schedule
    Owner findOwnerByDailySchedule(DailySchedule dailySchedule);
}
