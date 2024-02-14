package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
    
    Customer findCustomerById(Integer id);
    List<Customer> findByLastnameIgnoreCase(String lastname);
    boolean existsByCustomerAndScheduledCourse(Customer customer, ScheduledCourse scheduledCourse);
}
