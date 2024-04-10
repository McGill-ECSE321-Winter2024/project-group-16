package ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.utils.Helper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class DailyScheduleService {

    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    /**
     * Creates a new daily schedule with the default opening and closing times.
     * This method is only called when a new owner is created, and as there can only be 1 owner,
     * this limits the number of daily schedules to 7.
     * 
     * THIS METHOD SHOULD ONLY BE CALLED IN UserService.createOwner().
     * @return the new DailySchedule
     */
    @Transactional
    public List<DailySchedule> createDailySchedule() {
        List<DailySchedule> dailyScheduleList = new ArrayList<DailySchedule>();
        for (int i = 0; i < 7; i++) {
            DailySchedule dailySchedule = new DailySchedule();
            dailySchedule.setOpeningTime(Time.valueOf("08:00:00"));
            dailySchedule.setClosingTime(Time.valueOf("22:00:00"));
            dailyScheduleRepository.save(dailySchedule);
            dailyScheduleList.add(dailySchedule);
        }
        return dailyScheduleList;
    }

    /**
     * Returns a list of all daily schedules.
     * This list is always of size 7, with each index corresponding to a day of the week.
     * @return the list of all daily schedules
     */
    @Transactional
    public List<DailySchedule> getAllDailySchedules() {
        List<DailySchedule> dailySchedules = Helper.toList(dailyScheduleRepository.findAll());
        if (dailySchedules.isEmpty()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "The opening hours have not been set.");
        }
        // Sort the list by ID
        Collections.sort(dailySchedules);
        return dailySchedules;
    }

    /**
     * Returns the daily schedule with the given ID.
     * ID is the index of the day of the week.
     * 
     * 0: Sunday
     * 1: Monday
     * 2: Tuesday
     * 3: Wednesday
     * 4: Thursday
     * 5: Friday
     * 6: Saturday
     * @return the daily schedule with the given ID
     */
    @Transactional
    public DailySchedule getDailyScheduleById(int id) {
        List<DailySchedule> dailySchedules = getAllDailySchedules();
        return dailySchedules.get(id);
    }

    /**
     * Updates the opening and closing times of the daily schedule with the given ID.
     * 
     * Given information is validated.
     * @param id
     * @param openingTime
     * @param closingTime
     * @return the updated daily schedule
     */
    @Transactional
    public DailySchedule updateDailyScheduleByID(int id, Time openingTime, Time closingTime) {
        DailySchedule dailySchedule = getDailyScheduleById(id);
        if (openingTime == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Opening time must be provided.");
        }
        if (closingTime == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Closing time must be provided.");
        }
        if (openingTime.after(closingTime)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Opening time must be before closing time.");
        }
        dailySchedule.setOpeningTime(openingTime);
        dailySchedule.setClosingTime(closingTime);
        dailyScheduleRepository.save(dailySchedule);
        return dailySchedule;
    }
}