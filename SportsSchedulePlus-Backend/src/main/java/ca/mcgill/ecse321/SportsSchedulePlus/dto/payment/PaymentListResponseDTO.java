package ca.mcgill.ecse321.SportsSchedulePlus.dto.payment;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.payment.PaymentResponseDTO;

import java.util.List;

public class PaymentListResponseDTO {
    
    private List<PaymentResponseDTO> payments;
    
    public PaymentListResponseDTO(List<PaymentResponseDTO> payments) {
        this.payments = payments;
    }
    
    public List<PaymentResponseDTO> getPayments() {
        return payments;
    }
    
    public void setPayments(List<PaymentResponseDTO> payments) {
        this.payments = payments;
    }
}
