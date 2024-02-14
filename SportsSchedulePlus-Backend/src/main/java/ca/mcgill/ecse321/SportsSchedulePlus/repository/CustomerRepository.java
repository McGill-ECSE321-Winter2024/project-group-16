package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;


public interface CustomerRepository extends CrudRepository<Customer,Long> {
    
 
   Customer findCustomerById(Long id);

}