package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

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
