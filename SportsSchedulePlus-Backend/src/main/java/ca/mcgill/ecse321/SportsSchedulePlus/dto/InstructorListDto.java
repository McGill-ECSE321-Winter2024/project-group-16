package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.ArrayList;
import java.util.List;

public class InstructorListDto {

    private List<InstructorResponseDto> instructors;

    public InstructorListDto(List<InstructorResponseDto> instructors) {
        this.instructors = new ArrayList<>();
        for (InstructorResponseDto instructor : instructors) {
            this.instructors.add(instructor);
        }
    }

    public List<InstructorResponseDto> getInstructors() {
        return instructors;
    }
}