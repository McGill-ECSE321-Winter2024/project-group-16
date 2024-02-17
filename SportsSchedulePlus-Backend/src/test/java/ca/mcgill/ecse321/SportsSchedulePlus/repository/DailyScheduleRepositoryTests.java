package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class DailyScheduleRepositoryTests {

    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    @AfterEach
    public void clearDatabase(){
        dailyScheduleRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadDailySchedule(){

        // Create Daily Schedule
        Time open = Time.valueOf(LocalTime.of(8,0));
        Time close = Time.valueOf(LocalTime.of(17,0));

        DailySchedule dailySchedule = new DailySchedule();
        dailySchedule.setOpeningTime(open);
        dailySchedule.setClosingTime(close);

        // Save Daily Schedule
        dailyScheduleRepository.save(dailySchedule);

        // Read Daily Schedule from Database
        List<DailySchedule> foundOpeningTimes = dailyScheduleRepository.findDailyScheduleByOpeningTime(open);
        List<DailySchedule> foundClosingTimes = dailyScheduleRepository.findDailyScheduleByClosingTime(close);

        // Assertions
        assertNotNull(foundOpeningTimes);
        assertNotNull(foundClosingTimes);
        assertEquals(open, foundOpeningTimes.get(0).getOpeningTime());
        assertEquals(close, foundClosingTimes.get(0).getClosingTime());






    }


}
