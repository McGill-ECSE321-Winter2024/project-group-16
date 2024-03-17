package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.service.registrationservice.RegistrationService;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.RegistrationResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.RegistrationListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;

/**
 * Rest Controller that handles CRUD on Registration
 *
 * @author Vladimir Venkov
 */
@CrossOrigin(origins = "*")
@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Retrieves all registrations
     * @return RegistrationListResponseDTO
     */
    @GetMapping(value = {"/registrations", "/registrations/"})
    public RegistrationListResponseDTO getAllRegistrations() {
        List<RegistrationResponseDTO> registrationResponseDTOS = new ArrayList<>();
        for (Registration registration : registrationService.getAllRegistrations()) {
            registrationResponseDTOS.add(new RegistrationResponseDTO(registration));
        }
        return new RegistrationListResponseDTO(registrationResponseDTOS);
    }

    /**
     * Retrieves a registration by its confirmation number
     * @param confirmationNumber
     * @return RegistrationResponseDTO
     */
    @GetMapping(value = {"/registrations/{confirmationNumber}", "/registrations/{confirmationNumber}/"})
    public RegistrationResponseDTO getRegistrationByConfirmationNumber(@PathVariable("confirmationNumber") int confirmationNumber) {
        return new RegistrationResponseDTO(registrationService.getRegistrationByConfirmationNumber(confirmationNumber));
    }

    /**
     * Retrieves a customer's registrations
     * @param customerId
     * @return RegistrationListResponseDTO
     */
    @GetMapping(value = {"/customers/{customerID}/payments", "/customers/{customerID}/registrations/"})
    public RegistrationListResponseDTO getRegistrationsByCustomer(@PathVariable("customerID") int customerId) {
        List<RegistrationResponseDTO> registrationResponseDTOS = new ArrayList<>();
        for (Registration registration : registrationService.getRegistrationsByCustomer(customerId)) {
            registrationResponseDTOS.add(new RegistrationResponseDTO(registration));
        }
        return new RegistrationListResponseDTO(registrationResponseDTOS);
    }

    /**
     * Retrieves a course's registrations
     * @param courseId
     * @return RegistrationListResponseDTO
     */
    @GetMapping(value = {"/courses/{courseID}/registrations", "/courses/{courseID}/registrations/"})
    public RegistrationListResponseDTO getRegistrationsByCourse(@PathVariable("courseID") int courseId) {
        List<RegistrationResponseDTO> registrationResponseDTOS = new ArrayList<>();
        for (Registration registration : registrationService.getRegistrationsByCourse(courseId)) {
            registrationResponseDTOS.add(new RegistrationResponseDTO(registration));
        }
        return new RegistrationListResponseDTO(registrationResponseDTOS);
    }
   
    /**
     * Creates a new registration for a customer and course
     * The first path variable is the customer ID, the second one is the course ID.
     * @param customerId
     * @param courseId
     * @return RegistrationResponseDTO 
     */
    @PostMapping(value = {"/registrations/{customerID}/{courseID}", "/registrations/{customerID}/{courseID}/"})
    public RegistrationResponseDTO createRegistration(@PathVariable("customerID") int customerId, @PathVariable("courseID") int courseId) {
        Registration newRegistration = registrationService.createRegistration(customerId, courseId);
        RegistrationResponseDTO registrationDTO = new RegistrationResponseDTO(newRegistration);
        return registrationDTO;
    }

}