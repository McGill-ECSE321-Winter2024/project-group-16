package ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class ScheduledCourseResponseDTO {
    private int id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String location;
    private CourseTypeResponseDTO courseType; 


    // Constructor that takes a ScheduledCourse as input
    public ScheduledCourseResponseDTO(ScheduledCourse scheduledCourse) {
        this.id = scheduledCourse.getId();
        this.date = scheduledCourse.getDate();
        this.startTime = scheduledCourse.getStartTime();
        this.endTime = scheduledCourse.getEndTime();
        this.location = scheduledCourse.getLocation();
        
        // There's a constructor in CourseTypeResponseDto that takes a CourseType as input
        this.courseType = new CourseTypeResponseDTO(scheduledCourse.getCourseType());

    }

    public ScheduledCourseResponseDTO(int id, Date date, Time startTime, Time endTime, String location, CourseTypeResponseDTO courseType) {
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

    public CourseTypeResponseDTO getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeResponseDTO courseType) {
        this.courseType = courseType;
    }
}
