package ca.mcgill.ecse321.SportsSchedulePlus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.ScheduledCourseService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduledCourseServiceTests {

  @Mock
  private ScheduledCourseRepository scheduledCourseheduledCourseRepository;

  @Mock
  private CourseTypeRepository courseTypeRepository;

  @Mock
  private InstructorRepository instructorRepository;

  @Mock
  private DailyScheduleService dailyScheduleService; // Mock the DailyScheduleService

  @InjectMocks
  private ScheduledCourseService scheduledCourseheduledCourseService;

  @Mock
  private RegistrationRepository registrationRepository;

  @InjectMocks
  private CourseTypeService courseTypeService;

  private static final int SCHEDULED_COURSE_ID = 1;
  private static final Date DATE = Date.valueOf("2024-04-15");
  private static final Time START_TIME = Time.valueOf("09:00:00");
  private static final Time END_TIME = Time.valueOf("11:00:00");
  private static final String LOCATION = "Downtown Gym";
  private static final int COURSE_TYPE_ID = 10;
  private static final CourseType COURSE_TYPE = new CourseType();

  private static final DailySchedule MOCK_DAILY_SCHEDULE = new DailySchedule();
  static {
    MOCK_DAILY_SCHEDULE.setOpeningTime(Time.valueOf("08:00:00"));
    MOCK_DAILY_SCHEDULE.setClosingTime(Time.valueOf("20:00:00"));
  }

  @AfterEach
  public void tearDown() {
    Mockito.reset(scheduledCourseheduledCourseRepository);
  }

  @BeforeEach
  public void setMockOutput() {
    // Initialize CourseType for use in tests
    CourseType testCourseType = new CourseType();
    testCourseType.setId(10);
    testCourseType.setName("yoga");
    testCourseType.setApprovedByOwner(true);
    testCourseType.setPrice(20.0f);

    COURSE_TYPE.setName("Yoga");
    COURSE_TYPE.setApprovedByOwner(true);
    COURSE_TYPE.setPrice(20.0f);
    COURSE_TYPE.setId(COURSE_TYPE_ID);
    // Whenever anything is saved, just return the parameter object
    Answer < ? > returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(courseTypeRepository.save(any(CourseType.class))).thenAnswer(returnParameterAsAnswer);

    // Mock the behavior for courseTypeRepository.findById
  
    // Mock the behavior for scheduledCourseheduledCourseRepository.save
    // This ensures that any ScheduledCourse saved is returned as is, simulating persistence
    lenient().when(scheduledCourseheduledCourseRepository.save(any(ScheduledCourse.class)))
      .thenAnswer(invocation -> {
        ScheduledCourse scheduledCourse = invocation.getArgument(0);
        if (scheduledCourse.getId() <= 0) scheduledCourse.setId(SCHEDULED_COURSE_ID); // Simulate ID assignment on save
        return scheduledCourse;
      });

    // Mock the behavior for scheduledCourseheduledCourseRepository.findById
    // You may need to adjust this to return a specific ScheduledCourse for certain ID values
    lenient().when(scheduledCourseheduledCourseRepository.findById(anyInt()))
      .thenAnswer(invocation -> {
        Integer id = invocation.getArgument(0);
        if (id.equals(SCHEDULED_COURSE_ID)) {
          ScheduledCourse scheduledCourse = new ScheduledCourse();
          scheduledCourse.setId(SCHEDULED_COURSE_ID);
          scheduledCourse.setDate(DATE);
          scheduledCourse.setStartTime(START_TIME);
          scheduledCourse.setEndTime(END_TIME);
          scheduledCourse.setLocation(LOCATION);
          scheduledCourse.setCourseType(COURSE_TYPE);
          return Optional.of(scheduledCourse);
        }
        return Optional.empty();
      });

    // Initialize and set attributes for MOCK_DAILY_SCHEDULE
    DailySchedule MOCK_DAILY_SCHEDULE = new DailySchedule();
    MOCK_DAILY_SCHEDULE.setOpeningTime(Time.valueOf("08:00:00"));
    MOCK_DAILY_SCHEDULE.setClosingTime(Time.valueOf("20:00:00"));
    // Include setting other necessary attributes for MOCK_DAILY_SCHEDULE as needed

    // Mock the behavior for dailyScheduleService.getDailyScheduleById
    lenient().when(dailyScheduleService.getDailyScheduleById(anyInt()))
      .thenReturn(MOCK_DAILY_SCHEDULE);

  }

  @Test
  public void testCreateScheduledCourse() {
    Instructor tInstructor = new Instructor(1, "");
    when(instructorRepository.findById(1)).thenReturn(Optional.of(tInstructor));

    CourseType tCourseType = new CourseType();
    tCourseType.setApprovedByOwner(true);
    tCourseType.setId(5);
    when(courseTypeRepository.findById(5)).thenReturn(Optional.of(tCourseType));
    ScheduledCourse scheduledCourse = null;
    try {
      scheduledCourse = scheduledCourseheduledCourseService.createScheduledCourse("2024-04-15", "09:00:00", "11:00:00", LOCATION, 1, 5);
    } catch (SportsScheduleException e) {
      fail(e.getMessage());
    }
    verify(courseTypeRepository).findById(5);

    assertNotNull(scheduledCourse);
    assertEquals(DATE, scheduledCourse.getDate());
    assertEquals(START_TIME, scheduledCourse.getStartTime());
    assertEquals(END_TIME, scheduledCourse.getEndTime());
    assertEquals(LOCATION, scheduledCourse.getLocation());
    assertEquals(COURSE_TYPE, scheduledCourse.getCourseType());
    assertNotNull(scheduledCourse.getCourseType(), "CourseType should not be null.");
    assertEquals(5, scheduledCourse.getCourseType().getId(), "CourseType ID does not match the expected value.");
  }

  @Test
  public void testCreateScheduledCourseWithInvalidTime() {
    Instructor tInstructor = new Instructor(1, "");
    when(instructorRepository.findById(1)).thenReturn(Optional.of(tInstructor));

    CourseType tCourseType = new CourseType();
    tCourseType.setApprovedByOwner(true);
    tCourseType.setId(5);
    when(courseTypeRepository.findById(5)).thenReturn(Optional.of(tCourseType));

    Exception exception = assertThrows(SportsScheduleException.class, () -> {
      scheduledCourseheduledCourseService.createScheduledCourse("2024-04-15", "09:00:00", "08:00:00", LOCATION, 1, 5);

    });

    String expectedMessage = "End time must be after start time.";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testUpdateScheduledCourse() {
    // Setup the original ScheduledCourse that will be returned by the mock repository

    CourseType tCourseType = new CourseType();
    tCourseType.setId(COURSE_TYPE_ID);
    when(courseTypeRepository.findById(COURSE_TYPE_ID)).thenReturn(Optional.of(tCourseType));

    ScheduledCourse original = new ScheduledCourse();
    original.setId(SCHEDULED_COURSE_ID);
    original.setDate(DATE);
    original.setStartTime(START_TIME);
    original.setEndTime(END_TIME);
    original.setLocation(LOCATION);
    original.setCourseType(COURSE_TYPE);

    // Mock the repository call to return an Optional of the original ScheduledCourse
    when(scheduledCourseheduledCourseRepository.findById(SCHEDULED_COURSE_ID)).thenReturn(Optional.of(original));

    // Call the method under test with new values for the scheduledCourseheduled course
    ScheduledCourse updated = scheduledCourseheduledCourseService.updateScheduledCourse(SCHEDULED_COURSE_ID, "2024-05-15", "10:00:00", "12:00:00", "Uptown Gym", COURSE_TYPE_ID);

    // Assertions to verify the updated ScheduledCourse properties
    assertNotNull(updated, "The updated scheduledCourseheduled course should not be null.");
    assertEquals(Date.valueOf("2024-05-15"), updated.getDate(), "The date did not update correctly.");
    assertEquals(Time.valueOf("10:00:00"), updated.getStartTime(), "The start time did not update correctly.");
    assertEquals(Time.valueOf("12:00:00"), updated.getEndTime(), "The end time did not update correctly.");
    assertEquals("Uptown Gym", updated.getLocation(), "The location did not update correctly.");
    assertEquals(COURSE_TYPE, updated.getCourseType(), "The course type did not update correctly.");
  }

  @Test
  public void testDeleteScheduledCourse() {
    doNothing().when(scheduledCourseheduledCourseRepository).deleteById(anyInt());
    assertDoesNotThrow(() -> scheduledCourseheduledCourseService.deleteScheduledCourse(SCHEDULED_COURSE_ID));
    verify(scheduledCourseheduledCourseRepository, times(1)).deleteById(eq(SCHEDULED_COURSE_ID));
  }

  @Test
  public void testGetScheduledCourse() {
    // Setup: Create a new ScheduledCourse for testing
    ScheduledCourse testScheduledCourse = new ScheduledCourse();
    testScheduledCourse.setId(SCHEDULED_COURSE_ID);
    // Additional setup for testScheduledCourse as needed

    // Correctly mock the findById method to return an Optional of testScheduledCourse

    // Execution: Attempt to retrieve the ScheduledCourse by ID
    ScheduledCourse found = scheduledCourseheduledCourseService.getScheduledCourse(SCHEDULED_COURSE_ID);

    // Assertions: Check that the retrieved ScheduledCourse is not null and has the expected ID
    assertNotNull(found, "The retrieved ScheduledCourse should not be null.");
    assertEquals(SCHEDULED_COURSE_ID, found.getId(), "The ID of the retrieved ScheduledCourse does not match the expected value.");
  }

  @Test
  public void testGetScheduledCourseNotFound() {
    when(scheduledCourseheduledCourseRepository.findById(anyInt())).thenReturn(Optional.empty());

    Exception exception = assertThrows(SportsScheduleException.class, () -> {
      scheduledCourseheduledCourseService.getScheduledCourse(999); // Assuming 999 does not exist
    });

    assertEquals("There is no scheduled course with ID 999.", exception.getMessage());
  }

  @Test
  public void testGetAllScheduledCourses() {
    List < ScheduledCourse > scheduledCourseheduledCourses = new ArrayList < > ();
    scheduledCourseheduledCourses.add(new ScheduledCourse());
    when(scheduledCourseheduledCourseRepository.findAll()).thenReturn(scheduledCourseheduledCourses);

    List < ScheduledCourse > found = scheduledCourseheduledCourseService.getAllScheduledCourses();

    assertFalse(found.isEmpty());
    assertEquals(scheduledCourseheduledCourses.size(), found.size());
  }

  @Test
  public void testGetScheduledCoursesByLocation() {
    List < ScheduledCourse > scheduledCourseheduledCourses = new ArrayList < > ();
    ScheduledCourse scheduledCourse = new ScheduledCourse();
    scheduledCourse.setLocation(LOCATION);
    scheduledCourseheduledCourses.add(scheduledCourse);
    when(scheduledCourseheduledCourseRepository.findScheduledCourseByLocation(LOCATION)).thenReturn(scheduledCourseheduledCourses);

    List < ScheduledCourse > found = scheduledCourseheduledCourseService.getScheduledCoursesByLocation(LOCATION);

    assertFalse(found.isEmpty());
    assertEquals(LOCATION, found.get(0).getLocation());
  }

  @Test
  public void testGetScheduledCoursesByDate() {
    List < ScheduledCourse > scheduledCourseheduledCourses = new ArrayList < > ();
    ScheduledCourse scheduledCourse = new ScheduledCourse();
    scheduledCourse.setDate(DATE);
    scheduledCourseheduledCourses.add(scheduledCourse);
    when(scheduledCourseheduledCourseRepository.findScheduledCoursesByDate(DATE)).thenReturn(scheduledCourseheduledCourses);

    List < ScheduledCourse > found = scheduledCourseheduledCourseService.getScheduledCoursesByDate(DATE);

    assertFalse(found.isEmpty());
    assertEquals(DATE, found.get(0).getDate());
  }

  @Test
  public void testDeleteAllScheduledCourses() {
    // Mock the behavior for scheduledCourseheduledCourseRepository.deleteAll
    doNothing().when(scheduledCourseheduledCourseRepository).deleteAll();

    // Call the method under test
    scheduledCourseheduledCourseService.deleteAllScheduledCourses();

    // Verify that scheduledCourseheduledCourseRepository.deleteAll was called exactly once
    verify(scheduledCourseheduledCourseRepository, times(1)).deleteAll();
  }

  @Test
  public void testGetScheduledCoursesByStartTime() {
    List < ScheduledCourse > scheduledCourseheduledCourses = new ArrayList < > ();
    ScheduledCourse scheduledCourse = new ScheduledCourse();
    scheduledCourse.setStartTime(START_TIME);
    scheduledCourseheduledCourses.add(scheduledCourse);
    when(scheduledCourseheduledCourseRepository.findScheduledCoursesByStartTime(START_TIME)).thenReturn(scheduledCourseheduledCourses);

    List < ScheduledCourse > found = scheduledCourseheduledCourseService.getScheduledCoursesByStartTime(START_TIME);

    assertFalse(found.isEmpty());
    assertEquals(START_TIME, found.get(0).getStartTime());
  }

  @Test
  public void testGetScheduledCoursesByEndTime() {
    List < ScheduledCourse > scheduledCourseheduledCourses = new ArrayList < > ();
    ScheduledCourse scheduledCourse = new ScheduledCourse();
    scheduledCourse.setEndTime(END_TIME);
    scheduledCourseheduledCourses.add(scheduledCourse);
    when(scheduledCourseheduledCourseRepository.findScheduledCoursesByEndTime(END_TIME)).thenReturn(scheduledCourseheduledCourses);

    List < ScheduledCourse > found = scheduledCourseheduledCourseService.getScheduledCoursesByEndTime(END_TIME);

    assertFalse(found.isEmpty());
    assertEquals(END_TIME, found.get(0).getEndTime());
  }

}