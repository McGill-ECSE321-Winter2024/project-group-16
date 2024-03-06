package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class ScheduledCourseResponseDto {
    private int id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String location;
    private CourseTypeResponseDto courseType; // Assuming you have a CourseTypeResponseDto


      // Constructor that takes a ScheduledCourse as input
    public ScheduledCourseResponseDto(ScheduledCourse scheduledCourse) {
        this.id = scheduledCourse.getId();
        this.date = scheduledCourse.getDate();
        this.startTime = scheduledCourse.getStartTime();
        this.endTime = scheduledCourse.getEndTime();
        this.location = scheduledCourse.getLocation();
        
        // There's a constructor in CourseTypeResponseDto that takes a CourseType as input
        this.courseType = new CourseTypeResponseDto(scheduledCourse.getCourseType());
    }

    public ScheduledCourseResponseDto(int id, Date date, Time startTime, Time endTime, String location, CourseTypeResponseDto courseType) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.courseType = courseType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CourseTypeResponseDto getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeResponseDto courseType) {
        this.courseType = courseType;
    }
}
