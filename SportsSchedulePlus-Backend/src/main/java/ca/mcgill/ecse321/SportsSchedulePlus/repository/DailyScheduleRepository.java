package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import org.springframework.data.repository.CrudRepository;

public interface DailyScheduleRepository extends CrudRepository<DailySchedule, Long> {

}
