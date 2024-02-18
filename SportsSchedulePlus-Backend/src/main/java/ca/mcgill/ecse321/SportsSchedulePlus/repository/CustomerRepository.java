package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;

/**
 * Interface for managing data related to Customers in the application
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    // Find Customer by id
    Customer findCustomerById(int id);
}
