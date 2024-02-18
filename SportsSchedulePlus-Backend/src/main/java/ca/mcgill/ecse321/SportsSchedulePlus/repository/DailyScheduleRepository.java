package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import org.springframework.data.repository.CrudRepository;

import java.sql.Time;
import java.util.List;

// Interface for managing data related to DailySchedules in the application
public interface DailyScheduleRepository extends CrudRepository<DailySchedule, Integer> {

    // Find DailySchedule by openingTime
    List<DailySchedule> findDailyScheduleByOpeningTime(Time openingTime);

    // Find DailySchedule by closingTime
    List<DailySchedule> findDailyScheduleByClosingTime(Time closingTime);
}
