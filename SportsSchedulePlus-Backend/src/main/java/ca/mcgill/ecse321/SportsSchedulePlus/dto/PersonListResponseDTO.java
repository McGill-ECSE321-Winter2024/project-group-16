package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class PersonListResponseDTO {
    private List<PersonResponseDTO> persons;
    
    public PersonListResponseDTO(List<PersonResponseDTO> persons) {
        this.persons = persons;
    }
    
    public List<PersonResponseDTO> getPersons() {
        return persons;
    }
    
    public void setPersons(List<PersonResponseDTO> persons) {
        this.persons = persons;
    }
}