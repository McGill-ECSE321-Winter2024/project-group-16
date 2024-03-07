package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class CustomerDto extends PersonRoleDto {

    private List<PaymentDto> customerPayments;
    private List<ScheduledCourseDto> coursesRegistered;

    public CustomerDto() {
        // Default constructor
    }

    public CustomerDto(int id, List<PaymentDto> customerPayments, List<ScheduledCourseDto> coursesRegistered) {
        super(id);
        this.customerPayments = customerPayments;
        this.coursesRegistered = coursesRegistered;
    }

       // Constructor to convert a Customer object to CustomerDto
    public CustomerDto(Customer customer) {
        super(customer.getId());
        // Assuming a constructor in PersonRoleDto that takes an ID as input

        this.customerPayments = new ArrayList<>();
        for (Payment payment : customer.getCustomerPayments()) {
            this.customerPayments.add(new PaymentDto(payment));
        }

        this.coursesRegistered = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : customer.getCoursesRegistered()) {
            this.coursesRegistered.add(new ScheduledCourseDto(scheduledCourse));
        }
    }
    

    // Getters and setters for each attribute

    public List<PaymentDto> getCustomerPayments() {
        return customerPayments;
    }

    public void setCustomerPayments(List<PaymentDto> customerPayments) {
        this.customerPayments = customerPayments;
    }

    public List<ScheduledCourseDto> getCoursesRegistered() {
        return coursesRegistered;
    }

    public void setCoursesRegistered(List<ScheduledCourseDto> coursesRegistered) {
        this.coursesRegistered = coursesRegistered;
    }


}
