package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import org.springframework.data.repository.CrudRepository;


public interface OwnerRepository extends CrudRepository<Owner, Integer> {
    Owner findOwnerBySuggestedCourseType (CourseType courseType);
    Owner findOwnerByApprovedCourseType(CourseType courseType);
    Owner findOwnerByDailySchedule(DailySchedule dailySchedule);

}
