package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.io.Serializable;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;

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
      // Constructor to convert a Payment object to PaymentDto
    public PaymentDto(Payment payment) {
        this.customer = new CustomerDto(payment.getKey().getCustomer());
        // Assuming a constructor in CustomerDto that takes a Customer object

        this.scheduledCourse = new ScheduledCourseDto(payment.getKey().getScheduledCourse());
        // Assuming a constructor in ScheduledCourseDto that takes a ScheduledCourse object

        this.confirmationNumber = payment.getConfirmationNumber();
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
