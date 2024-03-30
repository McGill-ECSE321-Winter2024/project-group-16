package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the DailyScheduleRepository.
 * The overridden equals method in the DailySchedule model is used for assertions.
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
   * Tests the persistence and loading of DailySchedule entities in the database.
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
  /**
   * Tests the case where DailySchedule objects are not found
   */
  @Test
  public void testByDailyScheduleNotFound(){

    // Create, but do not save save an empty daily schedule
    DailySchedule dailySchedule = new DailySchedule();

    // Find opening and closing time for empty daily schedule
    List<DailySchedule> foundOpening = dailyScheduleRepository.findDailyScheduleByOpeningTime(dailySchedule.getOpeningTime());
    List<DailySchedule> foundClosing = dailyScheduleRepository.findDailyScheduleByClosingTime(dailySchedule.getClosingTime());

    // Assert that both lists are empty.
    assertTrue(foundOpening.isEmpty());
    assertTrue(foundClosing.isEmpty());

  }

}