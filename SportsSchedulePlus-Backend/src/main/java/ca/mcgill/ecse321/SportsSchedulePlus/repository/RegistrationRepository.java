package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

import java.util.List;

/**
 * Interface for managing data related to Registration in the application
 */
public interface RegistrationRepository extends CrudRepository<Registration, Registration.Key> {

    // Find registration by confirmation number
    Registration findRegistrationByConfirmationNumber(int confirmationNumber);

    // Find registrations by customer
    List<Registration> findRegistrationsByKeyCustomer(Customer customer);

    // Find registrations by scheduled course
    List<Registration> findRegistrationsByKeyScheduledCourse(ScheduledCourse scheduledCourse);

    // Find registration by customer and scheduled course
    Registration findRegistrationByKey(Registration.Key key);
}