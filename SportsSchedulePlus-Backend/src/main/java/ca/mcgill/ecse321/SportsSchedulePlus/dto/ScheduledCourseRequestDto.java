package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.sql.Date;
import java.sql.Time;

public class ScheduledCourseRequestDto {
    private int id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String location;
    private CourseTypeRequestDto courseType; // Assuming you have a CourseTypeRequestDto

    // Add any other fields or methods as needed

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

    public CourseTypeRequestDto getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeRequestDto courseType) {
        this.courseType = courseType;
    }
}
