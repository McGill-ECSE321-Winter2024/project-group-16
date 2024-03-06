package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.List;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.DailyScheduleRepository;
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
        DailySchedule ds = dailyScheduleRepository.findById(id).get();
        ds.setOpeningTime(openingTime);
        ds.setClosingTime(closingTime);
        dailyScheduleRepository.save(ds);
        return ds;
    }
}
