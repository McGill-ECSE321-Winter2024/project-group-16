package ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonRoleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;

public class CustomerRequestDTO extends PersonRoleResponseDTO {

    private boolean hasApplied;
    public CustomerRequestDTO() {
        hasApplied = false;
    }

    public CustomerRequestDTO(int id) {
        super(id);
        hasApplied = false;
    }



    // Constructor to convert a Customer object to CustomerDTO
    public CustomerRequestDTO(Customer customer) {
        super(customer.getId());
        this.hasApplied = customer.getHasApplied();
    }

    // Getters and setters for each attribute

    public boolean getHasApplied() {
        return hasApplied;
    }

    public void setHasApplied(boolean hasApplied) {
        this.hasApplied = hasApplied;
    }
}
