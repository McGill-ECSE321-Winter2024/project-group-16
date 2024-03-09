package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.utils.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.sql.Date;
import java.sql.Time;
import java.util.List;


@Service
public class ScheduledCourseService {

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private InstructorRepository instructorRepository;


  
    @Transactional
    public ScheduledCourse createScheduledCourse(int id, String date, String startTime, String endTime, String location, int courseTypeId) {
        // Parse date string
        LocalDate localDate = LocalDate.parse(date);
        // Convert LocalDate to java.sql.Date
        Date parsedDate = Date.valueOf(localDate);

        // Parse time strings
        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);

        // Use parsed values to create ScheduledCourse
        ScheduledCourse scheduledCourse = new ScheduledCourse();
        scheduledCourse.setId(id);
        scheduledCourse.setDate(parsedDate);
        scheduledCourse.setStartTime(parsedStartTime);
        scheduledCourse.setEndTime(parsedEndTime);
        scheduledCourse.setLocation(location);
        scheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));

        validateScheduledCourse(scheduledCourse);

        scheduledCourseRepository.save(scheduledCourse);
        return scheduledCourse;
    }

    private void validateScheduledCourse(ScheduledCourse scheduledCourse) {
        // Check if CourseType is not null
        if (scheduledCourse.getCourseType() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course must have a CourseType.");
        }
        if (scheduledCourse.getDate() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course date cannot be null.");
        }

        if (scheduledCourse.getStartTime() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course start time cannot be null.");
        }

        if (scheduledCourse.getEndTime() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course end time cannot be null.");
        }

        // Check if end time is greater than start time
        if (scheduledCourse.getEndTime().before(scheduledCourse.getStartTime())) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "End time must be after start time.");
        }
    }

    @Transactional
    public ScheduledCourse updateScheduledCourse(int id, String date, String startTime, String endTime,
                                                 String location, int courseTypeId) {
        // Find the existing scheduled course
        ScheduledCourse existingScheduledCourse = scheduledCourseRepository.findById(id);
    
        if (existingScheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Scheduled course not found");
        }
    
        // Parse date string
        LocalDate localDate = LocalDate.parse(date);
        // Convert LocalDate to java.sql.Date
        Date parsedDate = Date.valueOf(localDate);
    
        // Parse time strings
        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);
    
        // Update the existing scheduled course with the new values
        existingScheduledCourse.setDate(parsedDate);
        existingScheduledCourse.setStartTime(parsedStartTime);
        existingScheduledCourse.setEndTime(parsedEndTime);
        existingScheduledCourse.setLocation(location);
        existingScheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));
    
        // Validate the updated scheduled course
        validateScheduledCourse(existingScheduledCourse);
    
        // Save the updated scheduled course
        scheduledCourseRepository.save(existingScheduledCourse);
    
        return existingScheduledCourse;
    }

        @Transactional
    public List<Instructor> getInstructorsBySupervisedCourse(int scheduledCourseId) {
        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(scheduledCourseId);
        if (scheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Scheduled course not found with ID: " + scheduledCourseId);
        }
        List<Instructor> instructors = instructorRepository.findInstructorBySupervisedCourses(scheduledCourse);
        return instructors;
    }
    
    @Transactional
    public ScheduledCourse getScheduledCourse(int id) {
       ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(id);
       if(scheduledCourse == null){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
        }
       return scheduledCourse;
    }

    @Transactional
    public List<ScheduledCourse> getAllScheduledCourses() {
        return Helper.toList(scheduledCourseRepository.findAll());
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByLocation(String location) {
        return scheduledCourseRepository.findScheduledCourseByLocation(location);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByDate(Date date) {
        return scheduledCourseRepository.findScheduledCoursesByDate(date);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByCourseType(CourseType courseType) {
        return scheduledCourseRepository.findScheduledCoursesByCourseType(courseType);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByStartTime(Time startTime) {
        return scheduledCourseRepository.findScheduledCoursesByStartTime(startTime);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByEndTime(Time endTime) {
        return scheduledCourseRepository.findScheduledCoursesByEndTime(endTime);
    }

    @Transactional
    public void deleteScheduledCourse(int id) {
        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(id);
        if(scheduledCourse == null){
             throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
         }

        scheduledCourseRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllScheduledCourses() {
        List<ScheduledCourse> courses = Helper.toList(scheduledCourseRepository.findAll());

        if (courses == null || courses.isEmpty()){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There are no course types.");
        }
        
        scheduledCourseRepository.deleteAll();

    }



}
