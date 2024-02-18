package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.util.Helper;

/**
 * This class contains unit tests for the ScheduledCourseRepository.
 * The overridden equals method in the ScheduledCourse model is used for assertions.
 */
@SpringBootTest
public class ScheduledCourseRepositoryTests {

  @Autowired
  private ScheduledCourseRepository scheduledCourseRepository;

  @Autowired
  private CourseTypeRepository courseTypeRepository;

  /**
   * Cleanup the database after each test.
   */
  @AfterEach
  public void clearDatabase() {
    scheduledCourseRepository.deleteAll();
    courseTypeRepository.deleteAll();
  }

  /**
   * Test finding ScheduledCourse by location.
   */
  @Test
  public void testFindScheduledCourseByLocation() {
    // Create and save a ScheduledCourse
    CourseType courseType = new CourseType("Sample Description", true, 99.99f);
    courseTypeRepository.save(courseType);
    ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
    scheduledCourseRepository.save(scheduledCourse);
    // Find ScheduledCourse by location
    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCourseByLocation(scheduledCourse.getLocation());

    // Retrieve the found ScheduledCourse
    ScheduledCourse foundCourse = foundCourses.get(0);

    // Assert that the created ScheduledCourse matches the found ScheduledCourse
    assertEquals(scheduledCourse, foundCourse);
  }

  /**
   * Test finding ScheduledCourses by date.
   */
  @Test
  public void testFindScheduledCoursesByDate() {
    CourseType courseType = new CourseType("Sample Description", true, 99.99f);
    courseTypeRepository.save(courseType);
    ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
    scheduledCourseRepository.save(scheduledCourse);
    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByDate(scheduledCourse.getDate());

    // Retrieve the found ScheduledCourse
    ScheduledCourse foundCourse = foundCourses.get(0);

    // Assert that the created ScheduledCourse matches the found ScheduledCourse
    assertEquals(scheduledCourse, foundCourse);
  }

  /**
   * Test finding ScheduledCourses by course type.
   */
  @Test
  public void testFindScheduledCoursesByCourseType() {
    CourseType courseType = new CourseType("Sample Description", true, 99.99f);
    courseTypeRepository.save(courseType);
    ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
    scheduledCourseRepository.save(scheduledCourse);

    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByCourseType(scheduledCourse.getCourseType());

    // Retrieve the found ScheduledCourse
    ScheduledCourse foundCourse = foundCourses.get(0);

    // Assert that the created ScheduledCourse matches the found ScheduledCourse
    assertEquals(scheduledCourse, foundCourse);
  }

  /**
   * Test finding ScheduledCourses by start time.
   */
  @Test
  public void testFindScheduledCoursesByStartTime() {
    CourseType courseType = new CourseType("Sample Description", true, 99.99f);
    courseTypeRepository.save(courseType);
    ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
    scheduledCourseRepository.save(scheduledCourse);
    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByStartTime(scheduledCourse.getStartTime());

    // Retrieve the found ScheduledCourse
    ScheduledCourse foundCourse = foundCourses.get(0);

    // Assert that the created ScheduledCourse matches the found ScheduledCourse
    assertEquals(scheduledCourse, foundCourse);
  }

  /**
   * Test finding ScheduledCourses by end time.
   */
  @Test
  public void testFindScheduledCoursesByEndTime() {
    CourseType courseType = new CourseType("Sample Description", true, 99.99f);
    courseTypeRepository.save(courseType);
    ScheduledCourse scheduledCourse = Helper.createScheduledCourse(courseType);
    scheduledCourseRepository.save(scheduledCourse);

    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByEndTime(scheduledCourse.getEndTime());

    // Retrieve the found ScheduledCourse
    ScheduledCourse foundCourse = foundCourses.get(0);

    // Assert that the created ScheduledCourse matches the found ScheduledCourse
    assertEquals(scheduledCourse, foundCourse);
  }

  /**
   * Test finding no ScheduledCourses for an existing date with no courses
   */
  @Test
  public void testFindNoScheduledCoursesForDate() {
    // Attempt to find ScheduledCourses for an existing date with no courses
    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByDate(Date.valueOf("2024-01-01"));

    // Assert that no courses were found
    assertTrue(foundCourses.isEmpty());
  }

  /**
   *  Test finding no ScheduledCourses for an existing start time with no courses
   */
  @Test
  public void testFindNoScheduledCoursesForStartTime() {
    // Attempt to find ScheduledCourses for an existing start time with no courses
    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByStartTime(Time.valueOf("12:00:00"));

    // Assert that no courses were found
    assertTrue(foundCourses.isEmpty());
  }
   /**
    *  Test finding no ScheduledCourses for an existing end time with no courses
    */
  @Test
  public void testFindNoScheduledCoursesForEndTime() {
    // Attempt to find ScheduledCourses for an existing end time with no courses
    List <ScheduledCourse> foundCourses = scheduledCourseRepository.findScheduledCoursesByEndTime(Time.valueOf("13:00:00"));

    // Assert that no courses were found
    assertTrue(foundCourses.isEmpty());
  }


}