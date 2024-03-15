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

@Service
public class DailyScheduleService {

    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    @Transactional
    public List<DailySchedule> createDailySchedule() {
        List<DailySchedule> dsList = new ArrayList<DailySchedule>();
        for (int i = 0; i < 7; i++) {
            DailySchedule ds = new DailySchedule();
            ds.setOpeningTime(Time.valueOf("08:00:00"));
            ds.setClosingTime(Time.valueOf("22:00:00"));
            dailyScheduleRepository.save(ds);
            dsList.add(ds);
        }
        return dsList;
    }

    @Transactional
    public List<DailySchedule> getAllDailySchedules() {
        return Helper.toList(dailyScheduleRepository.findAll());
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
        DailySchedule ds = dailyScheduleRepository.findById(id).get();
        ds.setOpeningTime(openingTime);
        ds.setClosingTime(closingTime);
        dailyScheduleRepository.save(ds);
        return ds;
    }
}
