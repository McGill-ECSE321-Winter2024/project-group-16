package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.util.SportsScheduleException;


import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

@Service
public class ScheduledCourseService {

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private InstructorRepository instructorRepository;


    @Transactional
    public ScheduledCourse createScheduledCourse(int id, Date date, Time startTime, Time endTime, String location, CourseType courseType) {
        ScheduledCourse scheduledCourse = new ScheduledCourse(id, date, startTime, endTime, location, courseType);
        scheduledCourseRepository.save(scheduledCourse);
        return scheduledCourse;
    }

    @Transactional
    public ScheduledCourse getScheduledCourse(int id) {
        Optional<ScheduledCourse> scheduledCourse = scheduledCourseRepository.findById(id);
        if (scheduledCourse.isPresent()){
            ScheduledCourse scheduledCourse1 = scheduledCourse.get();
            return scheduledCourse1;
        }
        else{
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
        }
    }

}