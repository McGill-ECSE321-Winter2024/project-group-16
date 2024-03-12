package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.ArrayList;
import java.util.List;

public class InstructorListDTO {

    private List<InstructorResponseDTO> instructors;

    public InstructorListDTO(List<InstructorResponseDTO> instructors) {
        this.instructors = new ArrayList<>();
        for (InstructorResponseDTO instructor : instructors) {
            this.instructors.add(instructor);
        }
    }

    public List<InstructorResponseDTO> getInstructors() {
        return instructors;
    }
}
