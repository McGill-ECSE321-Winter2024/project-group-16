package ca.mcgill.ecse321.SportsSchedulePlus.service.registrationservice;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ca.mcgill.ecse321.SportsSchedulePlus.service.mailerservice.Mailer;
import ca.mcgill.ecse321.utils.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.beans.MailConfigBean;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration.Key;

/**
 * Service class for managing data related to Payments
 * @author Vladimir Venkov
 */
@Service
public class RegistrationService {
    
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    private Mailer mailer;

    /*
     * get all the registrations
     */
    @Transactional
    public List<Registration> getAllRegistrations() {
        return Helper.toList(registrationRepository.findAll());
    }

    /*
     * get registration by confirmation number
     */
    @Transactional
    public Registration getRegistrationByConfirmationNumber(int confirmationNumber) {
        Registration registration = registrationRepository.findRegistrationByConfirmationNumber(confirmationNumber);
        if (registration == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no payment with confirmation number " + confirmationNumber+".");
        }
        return registration;
    }

    /*
     * get all registrations for a customer
     */
    @Transactional
    public List<Registration> getRegistrationsByCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no customer with ID " + customerId + ".");
        }
        return registrationRepository.findRegistrationsByKeyCustomer(customer.get());
    }

    /*
     * get all registrations made for a course
     */
    @Transactional
    public List<Registration> getRegistrationsByCourse(int courseId) {
        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(courseId);
        if (scheduledCourse == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + courseId + ".");
        }
        return registrationRepository.findRegistrationsByKeyScheduledCourse(scheduledCourse);
    }

      // Method to send payment confirmation email
    private void sendPaymentConfirmationEmail(Registration payment) {
        try {
            String userEmail = personRepository.findById(payment.getKey().getCustomer().getId()).get().getEmail();
            String invoiceHtml = generateInvoiceHtml(payment);
            MailConfigBean mailSender = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
            mailer = new Mailer(mailSender);

            // Sending the email using the custom Mailer
            mailer.sendEmail("Payment Confirmation", "Thank you for your payment", invoiceHtml, userEmail);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    

    /*
     * Method to generate HTML content for the invoice
     */
    private String generateInvoiceHtml(Registration payment) {
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
     * create a new registration between a customer and a course
     * this method registers a customer to attend a scheduled course
     */
    @Transactional
    public Registration createRegistration(int customerId, int courseId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no customer with ID " + customerId + ".");
        }
        ScheduledCourse sceduledCourse = scheduledCourseRepository.findById(courseId);
        if (sceduledCourse == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + courseId + ".");
        }
        Key key = new Key(customer, sceduledCourse);
        Registration previousPayment = registrationRepository.findRegistrationByKey(key);
        if (previousPayment != null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The customer with ID " + customerId + " has already paid for the course with ID " + courseId + ".");
        }
        Registration registration = new Registration();
        int confirmationNumber = generateConfirmationNumber();
        registration.setConfirmationNumber(confirmationNumber);
        registration.setKey(key);
        registrationRepository.save(registration);
      
        // Send a payment confirmation email to the user
        sendPaymentConfirmationEmail(registration);
        return registration;
    }
    /**
     * Generates a confirmation number for a new registration
     * @return confirmation number
     */
    private int generateConfirmationNumber() {
        // Combine current timestamp with a sequential number
        // Incremental count from the registration repository
        long sequentialNumber = registrationRepository.count() + 1; 
        return Math.abs(LocalDateTime.now().hashCode() + (int)sequentialNumber);
    }
}