package ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.PaymentResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonRoleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

import java.util.ArrayList;
import java.util.List;

public class CustomerResponseDTO extends PersonRoleResponseDTO {

    private boolean hasApplied;
    private List<PaymentResponseDTO> customerPayments;
    private List<ScheduledCourseDTO> coursesRegistered;
    public CustomerResponseDTO() {
        customerPayments = new ArrayList<>();
        coursesRegistered = new ArrayList<>();
        hasApplied = false;
    }

    public CustomerResponseDTO(int id) {
        super(id);
        hasApplied = false;
    }

    public CustomerResponseDTO(int id, List<PaymentResponseDTO> customerPayments, List<ScheduledCourseDTO> coursesRegistered) {
        super(id);
        this.customerPayments = customerPayments;
        this.coursesRegistered = coursesRegistered;
        hasApplied = false;
    }

    // Constructor to convert a Customer object to CustomerDTO
    public CustomerResponseDTO(Customer customer) {
        super(customer.getId());

        this.customerPayments = new ArrayList<>();
        for (Payment payment : customer.getCustomerPayments()) {
            this.customerPayments.add(new PaymentResponseDTO(payment));
        }

        this.coursesRegistered = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : customer.getCoursesRegistered()) {
            this.coursesRegistered.add(new ScheduledCourseDTO(scheduledCourse));
        }

        this.hasApplied = customer.getHasApplied();
    }

    // Getters and setters for each attribute

    public List<PaymentResponseDTO> getCustomerPayments() {
        return customerPayments;
    }

    public void setCustomerPayments(List<PaymentResponseDTO> customerPayments) {
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
