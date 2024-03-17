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

    @Transactional
    public List<DailySchedule> getAllDailySchedules() {
        List<DailySchedule> dailySchedules = Helper.toList(dailyScheduleRepository.findAll());
        // Sort the list by ID
        Collections.sort(dailySchedules);
        return dailySchedules;
    }

    @Transactional
    public DailySchedule getDailyScheduleById(int id) {
        return dailyScheduleRepository.findById(id).get();
    }

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
        DailySchedule dailySchedule = dailyScheduleRepository.findById(id).get();
        dailySchedule.setOpeningTime(openingTime);
        dailySchedule.setClosingTime(closingTime);
        dailyScheduleRepository.save(dailySchedule);
        return dailySchedule;
    }
}