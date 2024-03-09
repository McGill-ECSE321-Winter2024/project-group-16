package ca.mcgill.ecse321.SportsSchedulePlus.service;


import java.util.Optional;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import jakarta.transaction.Transactional;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomService {

    @Autowired
    private CustomerRepository customerService;

    @Autowired
    private InstructorRepository instructorService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Transactional
    public void applyForInstructor(int customerId) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(true);
        customerService.save(c);
    }

    @Transactional
    public Instructor approveCustomer(int customerId) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Instructor i = new Instructor();
        for (Payment p : customer.get().getCustomerPayments()) {
            i.addCustomerPayment(p);
        }
        for (ScheduledCourse c : customer.get().getCoursesRegistered()) {
            i.addCoursesRegistered(c);
        }
        int id = customer.get().getId();
        customerRepository.delete(customer.get());
        i.setId(id);
        instructorService.save(i);
        return i;
    }

    @Transactional
    public void rejectCustomer(int customerId) {
        Optional<Customer> customer = customerService.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(false);
        customerService.save(c);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByWeek(Date monday, Date sunday) {
        return scheduledCourseRepository.findScheduledCoursesByDateBetween(monday, sunday);
    }

}
