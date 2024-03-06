package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ScheduledCourseDto {

    private int id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String location;
    private CourseTypeDto courseType;  // Assuming you have a CourseTypeDto class
    private List<PaymentDto> coursePayments;  // Assuming you have a PaymentDto class

    public ScheduledCourseDto() {
        // Default constructor
    }

    public ScheduledCourseDto(int id, Date date, Time startTime, Time endTime, String location,
                              CourseTypeDto courseType, List<PaymentDto> coursePayments) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.courseType = courseType;
        this.coursePayments = coursePayments;
    }

    // Getters and setters for each attribute

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

    public CourseTypeDto getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeDto courseType) {
        this.courseType = courseType;
    }

    public List<PaymentDto> getCoursePayments() {
        return coursePayments;
    }

    public void setCoursePayments(List<PaymentDto> coursePayments) {
        this.coursePayments = coursePayments;
    }
}
