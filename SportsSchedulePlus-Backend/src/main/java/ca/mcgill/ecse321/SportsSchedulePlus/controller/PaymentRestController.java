package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.service.MailConfigBean;
import ca.mcgill.ecse321.SportsSchedulePlus.service.Mailer;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PaymentService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PaymentResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PaymentListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;

/**
 * Rest controller for managing data related to Payments in the application
 * @author Vladimir Venkov
 */
@CrossOrigin(origins = "*")
@RestController
public class PaymentRestController {
    
    @Autowired
    private PaymentService paymentService;
    private  Mailer mailer; 
    @Autowired
    private PersonService personService;

    /*
     * get all payments
     */
    @GetMapping(value = {"/payments", "/payments/"})
    public PaymentListResponseDTO getAllPayments() {
        List<PaymentResponseDTO> dtos = new ArrayList<>();
        for (Payment p : paymentService.getAllPayments()) {
            dtos.add(new PaymentResponseDTO(p));
        }
        return new PaymentListResponseDTO(dtos);
    }

    /*
     * get payment by confirmation number
     */
    @GetMapping(value = { "/payments/{confirmationNumber}", "/payments/{confirmationNumber}/" })
    public PaymentResponseDTO getPaymentByConfirmationNumber(@PathVariable("confirmationNumber") int confirmationNumber) {
        return new PaymentResponseDTO(paymentService.getPaymentByConfirmationNumber(confirmationNumber));
    }

    /*
     * get payment by customer
     */
    @GetMapping(value = { "/customers/{customerID}/payments", "/customers/{customerID}/payments/" })
    public PaymentListResponseDTO getPaymentsByCustomer(@PathVariable("customerID") int customerId) {
        List<PaymentResponseDTO> dtos = new ArrayList<>();
        for (Payment p : paymentService.getPaymentsByCustomer(customerId)) {
            dtos.add(new PaymentResponseDTO(p));
        }
        return new PaymentListResponseDTO(dtos);
    }

    /*
     * get payment by scheduled course
     */
    @GetMapping(value = { "/courses/{courseID}/payments", "/courses/{courseID}/payments/" })
    public PaymentListResponseDTO getPaymentsByCourse(@PathVariable("courseID") int courseId) {
        List<PaymentResponseDTO> dtos = new ArrayList<>();
        for (Payment p : paymentService.getPaymentsByCourse(courseId)) {
            dtos.add(new PaymentResponseDTO(p));
        }
        return new PaymentListResponseDTO(dtos);
    }



    // Method to send payment confirmation email
    private void sendPaymentConfirmationEmail(Payment payment) {
        try {
            String userEmail = personService.getPersonById(payment.getKey().getCustomer().getId()).getEmail();
            String invoiceHtml = generateInvoiceHtml(payment);
            MailConfigBean mailSender = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
             mailer = new Mailer(mailSender);

            // Sending the email using the custom Mailer
            mailer.sendEmail("Payment Confirmation", "Thank you for your payment", invoiceHtml, userEmail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // Method to generate HTML content for the invoice
    private String generateInvoiceHtml(Payment payment) {
        StringBuilder html = new StringBuilder();
        String customerName = personService.getPersonById(payment.getKey().getCustomer().getId()).getName();
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
     * create a new payment between a customer and a course,
     * might need to /signup
     */
    @PutMapping(value = { "/payments/{customerID}/{courseID}", "/payments/{customerID}/{courseID}/" })
    public PaymentResponseDTO createPayment(@PathVariable("customerID") int customerId, @PathVariable("courseID") int courseId) {
      Payment newPayment = paymentService.createPayment(customerId, courseId);
      PaymentResponseDTO paymentDTO = new PaymentResponseDTO(newPayment);
      sendPaymentConfirmationEmail(newPayment);
      return paymentDTO;
    }
    
}