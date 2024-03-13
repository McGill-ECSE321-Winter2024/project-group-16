package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class ScheduledCourseDTO {

    private int id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String location;
    private CourseTypeResponseDTO courseType;  // Assuming you have a CourseTypeDto class
    private List<PaymentResponseDTO> coursePayments;  // Assuming you have a PaymentDto class

    public ScheduledCourseDTO() {
        // Default constructor
    }

        public ScheduledCourseDTO(ScheduledCourse scheduledCourse) {
        this.id = scheduledCourse.getId();
        this.date = scheduledCourse.getDate();
        this.startTime = scheduledCourse.getStartTime();
        this.endTime = scheduledCourse.getEndTime();
        this.location = scheduledCourse.getLocation();
        this.courseType = new CourseTypeResponseDTO(scheduledCourse.getCourseType());
        // Assuming a constructor in CourseTypeDto that takes a CourseType object

        // Assuming a similar constructor is available in PaymentDto
        for (Payment payment : scheduledCourse.getCoursePayments()) {
        PaymentResponseDTO paymentDto = new PaymentResponseDTO(payment);
        this.coursePayments.add(paymentDto);
        }
        // Assuming a static method in PaymentDto to convert a list of Payment objects to a list of PaymentDto objects
    }

    public ScheduledCourseDTO(int id, Date date, Time startTime, Time endTime, String location,
                              CourseTypeResponseDTO courseType, List<PaymentResponseDTO> coursePayments) {
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

    public CourseTypeResponseDTO getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeResponseDTO courseType) {
        this.courseType = courseType;
    }

    public List<PaymentResponseDTO> getCoursePayments() {
        return coursePayments;
    }

    public void setCoursePayments(List<PaymentResponseDTO> coursePayments) {
        this.coursePayments = coursePayments;
    }
}
