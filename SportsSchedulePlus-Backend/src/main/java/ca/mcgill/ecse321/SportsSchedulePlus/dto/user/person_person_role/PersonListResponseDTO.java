package ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role;

import java.util.List;

public class PersonListResponseDTO {
    private List<PersonDTO> persons;
    
    public PersonListResponseDTO(List<PersonDTO> persons) {
        this.persons = persons;
    }
    
    public List<PersonDTO> getPersons() {
        return persons;
    }
    
    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
}