package ca.mcgill.ecse321.SportsSchedulePlus.dto.payment;


import java.util.List;

public class RegistrationListResponseDTO {
    
    private List<RegistrationResponseDTO> registrations;
    
    public RegistrationListResponseDTO(List<RegistrationResponseDTO> registrations) {
        this.registrations = registrations;
    }
    
    public List<RegistrationResponseDTO> getRegistrations() {
        return registrations;
    }
    
    public void setRegistrations(List<RegistrationResponseDTO> registrations) {
        this.registrations = registrations;
    }
}
