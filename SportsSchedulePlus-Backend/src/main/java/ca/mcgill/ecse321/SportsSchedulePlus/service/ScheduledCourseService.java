package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.ArrayList;

@Service
public class ScheduledCourseService {

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Transactional
    public ScheduledCourse createScheduledCourse(int id, Date date, Time startTime, Time endTime, String location, CourseType courseType) {
        ScheduledCourse scheduledCourse = new ScheduledCourse(id, date, startTime, endTime, location, courseType);
        scheduledCourseRepository.save(scheduledCourse);
        return scheduledCourse;
    }
    @Transactional
    public ScheduledCourse createScheduledCourse(int id, Date date, Time startTime, Time endTime, String location, int courseTypeId) {
        // Retrieve the CourseType from the repository
        CourseType courseType = courseTypeRepository.findById(courseTypeId)
                .orElseThrow(() -> new IllegalArgumentException("CourseType not found"));

        // Create a new ScheduledCourse
        ScheduledCourse scheduledCourse = new ScheduledCourse(id, date, startTime, endTime, location, courseType);

        // Save the ScheduledCourse to the repository
        scheduledCourseRepository.save(scheduledCourse);

        return scheduledCourse;
    }


    @Transactional
    public ScheduledCourse getScheduledCourse(int id) {
        return scheduledCourseRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<ScheduledCourse> getAllScheduledCourses() {
        return toList(scheduledCourseRepository.findAll());
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
        scheduledCourseRepository.deleteById(id);
    }

    private <T> List<T> toList(Iterable<T> iterable){
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
