package ca.mcgill.ecse321.SportsSchedulePlus.dto.payment;


import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;

public class RegistrationDTO {

    private CustomerResponseDTO customer;
    private ScheduledCourseDTO scheduledCourse;
    private int confirmationNumber;

    public RegistrationDTO() {
        // Default constructor
    }

    public RegistrationDTO(CustomerResponseDTO customer, ScheduledCourseDTO scheduledCourse, int confirmationNumber) {
        this.customer = customer;
        this.scheduledCourse = scheduledCourse;
        this.confirmationNumber = confirmationNumber;
    }
      // Constructor to convert a Payment object to PaymentDto
    public RegistrationDTO(Registration payment) {
        this.customer = new CustomerResponseDTO(payment.getKey().getCustomer());
        // Assuming a constructor in CustomerDto that takes a Customer object

        this.scheduledCourse = new ScheduledCourseDTO(payment.getKey().getScheduledCourse());
        // Assuming a constructor in ScheduledCourseDto that takes a ScheduledCourse object

        this.confirmationNumber = payment.getConfirmationNumber();
    }


    // Getters and setters for each attribute

    public CustomerResponseDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponseDTO customer) {
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
