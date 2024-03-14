package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.service.PaymentService;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.PaymentResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.PaymentListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;

/**
 * Rest controller for managing data related to Payments in the application
 * @author Vladimir Venkov
 */
@CrossOrigin(origins = "*")
@RestController
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

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
    
     /*
     * create a new payment between a customer and a course,
     * might need to /register
     */
    @PutMapping(value = { "/payments/{customerID}/{courseID}", "/payments/{customerID}/{courseID}/" })
    public PaymentResponseDTO createPayment(@PathVariable("customerID") int customerId, @PathVariable("courseID") int courseId) {
      Payment newPayment = paymentService.createPayment(customerId, courseId);
      PaymentResponseDTO paymentDTO = new PaymentResponseDTO(newPayment);
      return paymentDTO;
    }
    
}