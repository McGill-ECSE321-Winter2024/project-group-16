package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.PaymentRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.beans.MailConfigBean;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
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
    private PersonRepository personRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    private Mailer mailer;

    /*
     * get all the payments
     */
    @Transactional
    public List<Payment> getAllPayments() {
        return Helper.toList(paymentRepository.findAll());
    }

    /*
     * get payment by confirmation number
     */
    @Transactional
    public Payment getPaymentByConfirmationNumber(int confirmationNumber) {
        Payment p = paymentRepository.findPaymentByConfirmationNumber(confirmationNumber);
        if ( p == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no payment with confirmation number " + confirmationNumber+".");
        }
        return p;
       
    }

    /*
     * get all payments for a customer
     */
    @Transactional
    public List<Payment> getPaymentsByCustomer(int customerId) {
        Optional<Customer> c = customerRepository.findById(customerId);
        if (!c.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no customer with ID " + customerId + ".");
        }
        return paymentRepository.findPaymentsByKeyCustomer(c.get());
    }

    /*
     * get all payments made for a course
     */
    @Transactional
    public List<Payment> getPaymentsByCourse(int courseId) {
        ScheduledCourse sc = scheduledCourseRepository.findById(courseId);
        if (sc == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + courseId + ".");
        }
        return paymentRepository.findPaymentsByKeyScheduledCourse(sc);
    }
      // Method to send payment confirmation email
    private void sendPaymentConfirmationEmail(Payment payment) {
        try {
            String userEmail = personRepository.findById(payment.getKey().getCustomer().getId()).get().getEmail();
            String invoiceHtml = generateInvoiceHtml(payment);
            MailConfigBean mailSender = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
            mailer = new Mailer(mailSender);

            // Sending the email using the custom Mailer
            mailer.sendEmail("Payment Confirmation", "Thank you for your payment", invoiceHtml, userEmail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /*
     * Method to generate HTML content for the invoice
     */
    private String generateInvoiceHtml(Payment payment) {
        StringBuilder html = new StringBuilder();
        String customerName = personRepository.findById(payment.getKey().getCustomer().getId()).get().getName();
        // Build the HTML content for the invoice
        html.append("<html>")
            .append("<body>")
            .append("<h2>Payment Confirmation</h2>")
            .append("<p>Thank you for your payment. Here are the details:</p>")
            .append("<p><strong>Confirmation Number:</strong> ").append(payment.getConfirmationNumber()).append("</p>")
            .append("<p><strong>Customer Name:</strong> ").append(customerName).append("</p>")
            .append("<p><strong>Course:</strong> ").append(payment.getKey().getScheduledCourse().getCourseType().getDescription()).append("</p>")
            .append("<p><strong>Amount:</strong> $").append(new DecimalFormat("0.00").format(payment.getKey().getScheduledCourse().getCourseType().getPrice())).append("</p>")
            .append("</body>")
            .append("</html>");
         

        return html.toString();
    }

    /*
     * create a new payment between a customer and a course
     * this method registers a customer to attend a scheduled course
     */
    @Transactional
    public Payment createPayment(int customerId, int courseId) {
        Customer c = customerRepository.findCustomerById(customerId);
        if (c == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no customer with ID " + customerId + ".");
        }
        ScheduledCourse sc = scheduledCourseRepository.findById(courseId);
        if (sc == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + courseId + ".");
        }
        Key key = new Key(c, sc);
        Payment previousPayment = paymentRepository.findPaymentByKey(key);
        if (previousPayment != null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The customer with ID " + customerId + " has already paid for the course with ID " + courseId + ".");
        }
        Payment p = new Payment();
        p.setKey(key);
        c.addCustomerPayment(p);
        c.addCoursesRegistered(sc);
        sc.addCoursePayment(p);
        paymentRepository.save(p);
        // Send a payment confirmation email to the user
        sendPaymentConfirmationEmail(p);
        return p;
    }
}