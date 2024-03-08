package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.io.Serializable;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;

public class PaymentDTO implements Serializable {

    private CustomerDTO customer;
    private ScheduledCourseDTO scheduledCourse;
    private int confirmationNumber;

    public PaymentDTO() {
        // Default constructor
    }

    public PaymentDTO(CustomerDTO customer, ScheduledCourseDTO scheduledCourse, int confirmationNumber) {
        this.customer = customer;
        this.scheduledCourse = scheduledCourse;
        this.confirmationNumber = confirmationNumber;
    }
      // Constructor to convert a Payment object to PaymentDto
    public PaymentDTO(Payment payment) {
        this.customer = new CustomerDTO(payment.getKey().getCustomer());
        // Assuming a constructor in CustomerDto that takes a Customer object

        this.scheduledCourse = new ScheduledCourseDTO(payment.getKey().getScheduledCourse());
        // Assuming a constructor in ScheduledCourseDto that takes a ScheduledCourse object

        this.confirmationNumber = payment.getConfirmationNumber();
    }


    // Getters and setters for each attribute

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public ScheduledCourseDTO getScheduledCourse() {
        return scheduledCourse;
    }

    public void setScheduledCourse(ScheduledCourseDTO scheduledCourse) {
        this.scheduledCourse = scheduledCourse;
    }

    public int getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
}
