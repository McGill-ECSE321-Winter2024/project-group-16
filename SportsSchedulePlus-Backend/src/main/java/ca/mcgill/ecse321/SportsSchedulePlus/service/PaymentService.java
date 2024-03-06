package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.PaymentRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment.Key;
import ca.mcgill.ecse321.utils.Helper;

/**
 * Service class for managing data related to Payments
 * @author Vladimir Venkov
 */
@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ScheduledCourseRepository ScheduledCourseRepository;

    @Transactional
    public List<Payment> getAllPayments() {
        return Helper.toList(paymentRepository.findAll());
    }

    @Transactional
    public Payment getPaymentByConfirmationNumber(int confirmationNumber) {
        return paymentRepository.findPaymentByConfirmationNumber(confirmationNumber);
    }

    @Transactional
    public List<Payment> getPaymentsByCustomer(int customerId) {
        Customer c = customerRepository.findById(customerId).get();
        if (c == null) {
            throw new IllegalArgumentException("There is no customer with ID " + customerId + ".");
        }
        return paymentRepository.findPaymentsByKeyCustomer(c);
    }

    @Transactional
    public List<Payment> getPaymentsByCourse(int courseId) {
        ScheduledCourse sc = ScheduledCourseRepository.findById(courseId).get();
        if (sc == null) {
            throw new IllegalArgumentException("There is no course with ID " + courseId + ".");
        }
        return paymentRepository.findPaymentsByKeyScheduledCourse(sc);
    }

    @Transactional
    public Payment createPayment(int customerId, int courseId) {
        Customer c = customerRepository.findById(customerId).get();
        if (c == null) {
            throw new IllegalArgumentException("There is no customer with ID " + customerId + ".");
        }
        ScheduledCourse sc = ScheduledCourseRepository.findById(courseId).get();
        if (sc == null) {
            throw new IllegalArgumentException("There is no course with ID " + courseId + ".");
        }
        List<Payment> previousPayments = getPaymentsByCustomer(customerId);
        if (previousPayments != null) {
            for (Payment p : previousPayments) {
                if (p.getKey().getScheduledCourse().equals(sc)) {
                    throw new IllegalArgumentException("The customer with ID " + customerId + " has already paid for the course with ID " + courseId + ".");
                }
            }
        }
        Key key = new Key(c, sc);
        Payment p = new Payment(key);
        return p;
    }
}
