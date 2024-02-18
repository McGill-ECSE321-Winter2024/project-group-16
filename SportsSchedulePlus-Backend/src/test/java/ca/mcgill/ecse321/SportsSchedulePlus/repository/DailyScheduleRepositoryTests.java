package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

/**
 * Test class for the DailyScheduleRepository.
 */
@SpringBootTest
public class DailyScheduleRepositoryTests {

  @Autowired
  private DailyScheduleRepository dailyScheduleRepository;

  /**
   * Clear the database after each test to ensure a clean state.
   */
  @AfterEach
  public void clearDatabase() {
    dailyScheduleRepository.deleteAll();
  }

  /**
   * Test the persistence and loading of DailySchedule entities in the database.
   */
  @Test
  public void testPersistAndLoadDailySchedule() {
    Time open = Time.valueOf(LocalTime.of(8, 0));
    Time close = Time.valueOf(LocalTime.of(17, 0));
    DailySchedule dailySchedule = new DailySchedule();
    dailySchedule.setOpeningTime(open);
    dailySchedule.setClosingTime(close);

    // Save Daily Schedule
    dailyScheduleRepository.save(dailySchedule);

    // Read Daily Schedule from Database
    List <DailySchedule> foundOpeningTimes = dailyScheduleRepository.findDailyScheduleByOpeningTime(open);
    List <DailySchedule> foundClosingTimes = dailyScheduleRepository.findDailyScheduleByClosingTime(close);

    // Assertions   
    // We use the overridden equals method in DailySchedule model for assertion
    assertEquals(dailySchedule, foundOpeningTimes.get(0));
    assertEquals(dailySchedule, foundClosingTimes.get(0));
  }

}