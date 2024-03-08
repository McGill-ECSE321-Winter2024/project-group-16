package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class CustomerDTO extends PersonRoleDto {

    private List<PaymentDto> customerPayments;
    private List<ScheduledCourseDTO> coursesRegistered;

    public CustomerDTO() {
        // Default constructor
    }

    public CustomerDTO(int id, List<PaymentDto> customerPayments, List<ScheduledCourseDTO> coursesRegistered) {
        super(id);
        this.customerPayments = customerPayments;
        this.coursesRegistered = coursesRegistered;
    }

       // Constructor to convert a Customer object to CustomerDto
    public CustomerDTO(Customer customer) {
        super(customer.getId());
        // Assuming a constructor in PersonRoleDto that takes an ID as input

        this.customerPayments = new ArrayList<>();
        for (Payment payment : customer.getCustomerPayments()) {
            this.customerPayments.add(new PaymentDto(payment));
        }

        this.coursesRegistered = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : customer.getCoursesRegistered()) {
            this.coursesRegistered.add(new ScheduledCourseDTO(scheduledCourse));
        }
    }
    

    // Getters and setters for each attribute

    public List<PaymentDto> getCustomerPayments() {
        return customerPayments;
    }

    public void setCustomerPayments(List<PaymentDto> customerPayments) {
        this.customerPayments = customerPayments;
    }

    public List<ScheduledCourseDTO> getCoursesRegistered() {
        return coursesRegistered;
    }

    public void setCoursesRegistered(List<ScheduledCourseDTO> coursesRegistered) {
        this.coursesRegistered = coursesRegistered;
    }


}