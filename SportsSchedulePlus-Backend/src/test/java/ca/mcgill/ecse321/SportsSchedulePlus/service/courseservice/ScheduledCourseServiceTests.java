package ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduledCourseServiceTests {

    @Mock
    private ScheduledCourseRepository scheduledCourseRepository;

    @Mock
    private CourseTypeRepository courseTypeRepository;

    @InjectMocks
    private ScheduledCourseService scheduledCourseService;

    private static final int SCHEDULED_COURSE_ID = 1;
    private static final Date DATE = Date.valueOf("2024-04-15");
    private static final Time START_TIME = Time.valueOf("09:00:00");
    private static final Time END_TIME = Time.valueOf("11:00:00");
    private static final String LOCATION = "Downtown Gym";
    private static final int COURSE_TYPE_ID = 10;
    private static final CourseType COURSE_TYPE = new CourseType();

    @BeforeEach
    public void setMockOutput() {
        lenient().when(courseTypeRepository.findById(anyInt())).thenReturn(Optional.of(COURSE_TYPE));
        lenient().when(scheduledCourseRepository.save(any(ScheduledCourse.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(scheduledCourseRepository.findById(anyInt())).thenReturn(new ScheduledCourse());
    }

    @Test
    public void testCreateScheduledCourse() {
        ScheduledCourse sc = null;
        try {
            sc = scheduledCourseService.createScheduledCourse("2024-04-15", "09:00:00", "11:00:00", LOCATION, COURSE_TYPE_ID);
        } catch (SportsScheduleException e) {
            fail(e.getMessage());
        }

        assertNotNull(sc);
        assertEquals(DATE, sc.getDate());
        assertEquals(START_TIME, sc.getStartTime());
        assertEquals(END_TIME, sc.getEndTime());
        assertEquals(LOCATION, sc.getLocation());
        assertEquals(COURSE_TYPE, sc.getCourseType());
    }

    @Test
    public void testCreateScheduledCourseWithInvalidTime() {
        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            scheduledCourseService.createScheduledCourse("2024-04-15", "11:00:00", "09:00:00", LOCATION, COURSE_TYPE_ID);
        });

        String expectedMessage = "End time must be after start time.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateScheduledCourse() {
        ScheduledCourse original = new ScheduledCourse();
        original.setId(SCHEDULED_COURSE_ID);
        original.setDate(DATE);
        original.setStartTime(START_TIME);
        original.setEndTime(END_TIME);
        original.setLocation(LOCATION);
        original.setCourseType(COURSE_TYPE);

        when(scheduledCourseRepository.findById(SCHEDULED_COURSE_ID)).thenReturn(original);

        ScheduledCourse updated = scheduledCourseService.updateScheduledCourse(SCHEDULED_COURSE_ID, "2024-05-15", "10:00:00", "12:00:00", "Uptown Gym", COURSE_TYPE_ID);

        assertNotNull(updated);
        assertEquals(Date.valueOf("2024-05-15"), updated.getDate());
        assertEquals(Time.valueOf("10:00:00"), updated.getStartTime());
        assertEquals(Time.valueOf("12:00:00"), updated.getEndTime());
        assertEquals("Uptown Gym", updated.getLocation());
        assertEquals(COURSE_TYPE, updated.getCourseType());
    }

    @Test
    public void testDeleteScheduledCourse() {
        doNothing().when(scheduledCourseRepository).deleteById(anyInt());
        assertDoesNotThrow(() -> scheduledCourseService.deleteScheduledCourse(SCHEDULED_COURSE_ID));
        verify(scheduledCourseRepository, times(1)).deleteById(eq(SCHEDULED_COURSE_ID));
    }

    // Continuing from the previous tests...

    @Test
    public void testGetScheduledCourse() {
    when(scheduledCourseRepository.findById(SCHEDULED_COURSE_ID)).thenReturn(new ScheduledCourse());

    ScheduledCourse found = scheduledCourseService.getScheduledCourse(SCHEDULED_COURSE_ID);

    assertNotNull(found);
    assertEquals(SCHEDULED_COURSE_ID, found.getId());
    }

    @Test
    public void testGetScheduledCourseNotFound() {
    when(scheduledCourseRepository.findById(anyInt())).thenReturn(null);

    Exception exception = assertThrows(SportsScheduleException.class, () -> {
        scheduledCourseService.getScheduledCourse(999); // Assuming 999 does not exist
    });

    assertEquals("There is no scheduled course with ID 999.", exception.getMessage());
    }

    @Test
    public void testGetAllScheduledCourses() {
    List<ScheduledCourse> scheduledCourses = new ArrayList<>();
    scheduledCourses.add(new ScheduledCourse());
    when(scheduledCourseRepository.findAll()).thenReturn(scheduledCourses);

    List<ScheduledCourse> found = scheduledCourseService.getAllScheduledCourses();

    assertFalse(found.isEmpty());
    assertEquals(scheduledCourses.size(), found.size());
    }

    @Test
    public void testGetScheduledCoursesByLocation() {
    List<ScheduledCourse> scheduledCourses = new ArrayList<>();
    ScheduledCourse sc = new ScheduledCourse();
    sc.setLocation(LOCATION);
    scheduledCourses.add(sc);
    when(scheduledCourseRepository.findScheduledCourseByLocation(LOCATION)).thenReturn(scheduledCourses);

    List<ScheduledCourse> found = scheduledCourseService.getScheduledCoursesByLocation(LOCATION);

    assertFalse(found.isEmpty());
    assertEquals(LOCATION, found.get(0).getLocation());
    }

    @Test
    public void testGetScheduledCoursesByDate() {
    List<ScheduledCourse> scheduledCourses = new ArrayList<>();
    ScheduledCourse sc = new ScheduledCourse();
    sc.setDate(DATE);
    scheduledCourses.add(sc);
    when(scheduledCourseRepository.findScheduledCoursesByDate(DATE)).thenReturn(scheduledCourses);

    List<ScheduledCourse> found = scheduledCourseService.getScheduledCoursesByDate(DATE);

    assertFalse(found.isEmpty());
    assertEquals(DATE, found.get(0).getDate());
    }

    @Test
    public void testDeleteAllScheduledCourses() {
    doNothing().when(scheduledCourseRepository).deleteAll();
    assertDoesNotThrow(() -> scheduledCourseService.deleteAllScheduledCourses());
    verify(scheduledCourseRepository, times(1)).deleteAll();
    }

    @Test
    public void testGetScheduledCoursesByStartTime() {
    List<ScheduledCourse> scheduledCourses = new ArrayList<>();
    ScheduledCourse sc = new ScheduledCourse();
    sc.setStartTime(START_TIME);
    scheduledCourses.add(sc);
    when(scheduledCourseRepository.findScheduledCoursesByStartTime(START_TIME)).thenReturn(scheduledCourses);

    List<ScheduledCourse> found = scheduledCourseService.getScheduledCoursesByStartTime(START_TIME);

    assertFalse(found.isEmpty());
    assertEquals(START_TIME, found.get(0).getStartTime());
    }

    @Test
    public void testGetScheduledCoursesByEndTime() {
    List<ScheduledCourse> scheduledCourses = new ArrayList<>();
    ScheduledCourse sc = new ScheduledCourse();
    sc.setEndTime(END_TIME);
    scheduledCourses.add(sc);
    when(scheduledCourseRepository.findScheduledCoursesByEndTime(END_TIME)).thenReturn(scheduledCourses);

    List<ScheduledCourse> found = scheduledCourseService.getScheduledCoursesByEndTime(END_TIME);

    assertFalse(found.isEmpty());
    assertEquals(END_TIME, found.get(0).getEndTime());
    }




    }
