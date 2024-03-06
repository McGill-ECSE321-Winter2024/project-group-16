package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.io.Serializable;

public class PaymentDto implements Serializable {

    private CustomerDto customer;
    private ScheduledCourseDto scheduledCourse;
    private int confirmationNumber;

    public PaymentDto() {
        // Default constructor
    }

    public PaymentDto(CustomerDto customer, ScheduledCourseDto scheduledCourse, int confirmationNumber) {
        this.customer = customer;
        this.scheduledCourse = scheduledCourse;
        this.confirmationNumber = confirmationNumber;
    }

    // Getters and setters for each attribute

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public ScheduledCourseDto getScheduledCourse() {
        return scheduledCourse;
    }

    public void setScheduledCourse(ScheduledCourseDto scheduledCourse) {
        this.scheduledCourse = scheduledCourse;
    }

    public int getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
}
