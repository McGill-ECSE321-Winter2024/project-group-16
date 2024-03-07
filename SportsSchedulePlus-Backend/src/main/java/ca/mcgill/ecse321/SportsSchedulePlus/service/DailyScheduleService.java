package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.List;
import java.sql.Time;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.utils.Helper;

/**
 * Service class for managing data related to the opening Hours
 * @author Vladimir Venkov
 */
@Service
public class DailyScheduleService {
    
    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    /*
     * get all the daily schedules
     */
    @Transactional
    public List<DailySchedule> getAllDailySchedules() {
        return Helper.toList(dailyScheduleRepository.findAll());
    }

    /*
     * get daily schedule by id
     */
    @Transactional
    public DailySchedule getDailyScheduleById(int id) {
        return dailyScheduleRepository.findById(id).get();
    }

    /*
     * update the opening hours for a day by its id
     */
    @Transactional
    public DailySchedule updateDailyScheduleByID(int id, Time openingTime, Time closingTime) {
        Optional<DailySchedule> aOptionalDailySchedule = dailyScheduleRepository.findById(id);
        if (!aOptionalDailySchedule.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no schedule with ID " + id + ".");
        }
        if (openingTime == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Opening time must be provided.");
        }
        if (closingTime == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Closing time must be provided.");
        }
        if (openingTime.after(closingTime)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Opening time must be before closing time.");
        }
        DailySchedule ds = dailyScheduleRepository.findById(id).get();
        ds.setOpeningTime(openingTime);
        ds.setClosingTime(closingTime);
        dailyScheduleRepository.save(ds);
        return ds;
    }
}
