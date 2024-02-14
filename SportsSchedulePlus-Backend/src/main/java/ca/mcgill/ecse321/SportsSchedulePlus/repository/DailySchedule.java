package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailySchedule extends CrudRepository<DailySchedule, Integer> {

    List<DailySchedule> findDailyScheduleByOwner(Owner owner);

}
