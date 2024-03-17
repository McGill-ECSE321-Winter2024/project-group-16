package ca.mcgill.ecse321.SportsSchedulePlus.dto.payment;


import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;

public class RegistrationDTO {

    private CustomerRequestDTO customer;
    private ScheduledCourseResponseDTO scheduledCourse;
    private int confirmationNumber;

    public RegistrationDTO() {
        // Default constructor
    }

    public RegistrationDTO(CustomerRequestDTO customer, ScheduledCourseResponseDTO scheduledCourse, int confirmationNumber) {
        this.customer = customer;
        this.scheduledCourse = scheduledCourse;
        this.confirmationNumber = confirmationNumber;
    }
      // Constructor to convert a Payment object to PaymentDto
    public RegistrationDTO(Registration payment) {
        this.customer = new CustomerRequestDTO(payment.getKey().getCustomer());
        // Assuming a constructor in CustomerDto that takes a Customer object

        this.scheduledCourse = new ScheduledCourseResponseDTO(payment.getKey().getScheduledCourse());
        // Assuming a constructor in ScheduledCourseDto that takes a ScheduledCourse object

        this.confirmationNumber = payment.getConfirmationNumber();
    }


    // Getters and setters for each attribute

    public CustomerRequestDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerRequestDTO customer) {
        this.customer = customer;
    }

    public ScheduledCourseResponseDTO getScheduledCourse() {
        return scheduledCourse;
    }

    public void setScheduledCourse(ScheduledCourseResponseDTO scheduledCourse) {
        this.scheduledCourse = scheduledCourse;
    }

    public int getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
}
