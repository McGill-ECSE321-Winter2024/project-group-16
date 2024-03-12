package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

import java.util.List;

/**
 * Interface for managing data related to Payments in the application
 */
public interface PaymentRepository extends CrudRepository<Payment, Payment.Key> {

    // Find Payments by confirmation number
    Payment findPaymentByConfirmationNumber(int confirmationNumber);

    // Find payments by customer
    List<Payment> findPaymentsByKeyCustomer(Customer customer);

    // Find payments by scheduled course
    List<Payment> findPaymentsByKeyScheduledCourse(ScheduledCourse scheduledCourse);

    // Find payments by customer and scheduled course
    Payment findPaymentByKey(Payment.Key key);
}