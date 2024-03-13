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
    private InstructorService instructorService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private PersonRepository personRepository;

    /*
     * customer applies to become instructor
     * changes the hasApplied attribute of the customer to true
     */
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

    /*
     * customer is approved to become instructor
     * this creates a new instructor with the same email as the customer
     * whilst deleting the old customer account
     * @return the new instructor
     */
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

    /*
     * customer is rejected to become instructor
     * changes the hasApplied attribute of the customer to false
     */
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

    /*
     * get all scheduled courses for a week
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByWeek(Date monday, Date sunday) {
        return scheduledCourseRepository.findScheduledCoursesByDateBetween(monday, sunday);
    }

}
