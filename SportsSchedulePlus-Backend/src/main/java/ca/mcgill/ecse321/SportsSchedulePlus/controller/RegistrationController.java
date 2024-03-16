package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.service.registrationservice.RegistrationService;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.RegistrationResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.RegistrationListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;

/**
 * Rest controller for managing data related to Payments in the application
 *
 * @author Vladimir Venkov
 */
@CrossOrigin(origins = "*")
@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /*
     * get all registrations
     */
    @GetMapping(value = {"/registrations", "/registrations/"})
    public RegistrationListResponseDTO getAllPayments() {
        List<RegistrationResponseDTO> dtos = new ArrayList<>();
        for (Registration registration : registrationService.getAllRegistrations()) {
            dtos.add(new RegistrationResponseDTO(registration));
        }
        return new RegistrationListResponseDTO(dtos);
    }

    /*
     * get registration by confirmation number
     */
    @GetMapping(value = {"/registrations/{confirmationNumber}", "/registrations/{confirmationNumber}/"})
    public RegistrationResponseDTO getPaymentByConfirmationNumber(@PathVariable("confirmationNumber") int confirmationNumber) {
        return new RegistrationResponseDTO(registrationService.getRegistrationByConfirmationNumber(confirmationNumber));
    }

    /*
     * get registration by customer
     */
    @GetMapping(value = {"/customers/{customerID}/payments", "/customers/{customerID}/registrations/"})
    public RegistrationListResponseDTO getPaymentsByCustomer(@PathVariable("customerID") int customerId) {
        List<RegistrationResponseDTO> dtos = new ArrayList<>();
        for (Registration registration : registrationService.getRegistrationsByCustomer(customerId)) {
            dtos.add(new RegistrationResponseDTO(registration));
        }
        return new RegistrationListResponseDTO(dtos);
    }

    /*
     * get registration by scheduled course
     */
    @GetMapping(value = {"/courses/{courseID}/registrations", "/courses/{courseID}/registrations/"})
    public RegistrationListResponseDTO getPaymentsByCourse(@PathVariable("courseID") int courseId) {
        List<RegistrationResponseDTO> dtos = new ArrayList<>();
        for (Registration registration : registrationService.getRegistrationsByCourse(courseId)) {
            dtos.add(new RegistrationResponseDTO(registration));
        }
        return new RegistrationListResponseDTO(dtos);
    }

    /*
     * create a new registration between a customer and a course,
     * might need to /register
     */
    @PutMapping(value = {"/registrations/{customerID}/{courseID}", "/registrations/{customerID}/{courseID}/"})
    public RegistrationResponseDTO createPayment(@PathVariable("customerID") int customerId, @PathVariable("courseID") int courseId) {
        Registration newRegistration = registrationService.createRegistration(customerId, courseId);
        RegistrationResponseDTO registrationDTO = new RegistrationResponseDTO(newRegistration);
        return registrationDTO;
    }

}