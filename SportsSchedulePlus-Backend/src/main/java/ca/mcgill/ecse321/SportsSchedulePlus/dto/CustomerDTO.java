package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

import java.util.ArrayList;
import java.util.List;

public class CustomerDTO extends PersonRoleDTO { 

    private boolean hasApplied;
    private List<PaymentDTO> customerPayments;
    private List<ScheduledCourseDTO> coursesRegistered;
    public CustomerDTO() {
        customerPayments = new ArrayList<>();
        coursesRegistered = new ArrayList<>();
        hasApplied = false;
    }

    public CustomerDTO(int id) {
        super(id);
        hasApplied = false;
    }

    public CustomerDTO(int id, List<PaymentDTO> customerPayments, List<ScheduledCourseDTO> coursesRegistered) {
        super(id);
        this.customerPayments = customerPayments;
        this.coursesRegistered = coursesRegistered;
        hasApplied = false;
    }

    // Constructor to convert a Customer object to CustomerDTO
    public CustomerDTO(Customer customer) {
        super(customer.getId());

        this.customerPayments = new ArrayList<>();
        for (Payment payment : customer.getCustomerPayments()) {
            this.customerPayments.add(new PaymentDTO(payment));
        }

        this.coursesRegistered = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : customer.getCoursesRegistered()) {
            this.coursesRegistered.add(new ScheduledCourseDTO(scheduledCourse));
        }

        this.hasApplied = customer.getHasApplied();
    }

    // Getters and setters for each attribute

    public List<PaymentDTO> getCustomerPayments() {
        return customerPayments;
    }

    public void setCustomerPayments(List<PaymentDTO> customerPayments) {
        this.customerPayments = customerPayments;
    }

    public List<ScheduledCourseDTO> getCoursesRegistered() {
        return coursesRegistered;
    }

    public void setCoursesRegistered(List<ScheduledCourseDTO> coursesRegistered) {
        this.coursesRegistered = coursesRegistered;
    }

    public boolean getHasApplied() {
        return hasApplied;
    }

    public void setHasApplied(boolean hasApplied) {
        this.hasApplied = hasApplied;
    }
}
