package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import org.springframework.data.repository.CrudRepository;

import java.sql.Time;
import java.util.List;
public interface DailyScheduleRepository extends CrudRepository<DailySchedule, Long> {

    List<DailySchedule> findDailyScheduleByOpeningTime(Time openingTime);
    List<DailySchedule> findDailyScheduleByClosingTime(Time closingTime);
}
