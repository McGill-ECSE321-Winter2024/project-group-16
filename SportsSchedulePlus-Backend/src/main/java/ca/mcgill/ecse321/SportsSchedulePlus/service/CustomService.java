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
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import org.springframework.stereotype.Service;

@Service
public class CustomService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private PersonRepository personRepository;


    @Transactional
    public void applyForInstructor(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(true);
        customerRepository.save(c);
    }

    @Transactional
    public Instructor approveCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Person p = personRepository.findPersonByPersonRole(customer.get());
        Person newP = instructorService.createInstructor(p.getEmail(), "");
        return (Instructor) newP.getPersonRole();
    }

    @Transactional
    public void rejectCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(false);
        customerRepository.save(c);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByWeek(Date monday, Date sunday) {
        return scheduledCourseRepository.findScheduledCoursesByDateBetween(monday, sunday);
    }

}
