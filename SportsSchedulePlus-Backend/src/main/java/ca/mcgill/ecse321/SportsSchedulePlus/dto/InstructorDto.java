package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;

public class InstructorDto extends PersonRoleDto{

    private String experience;
    private int id;

    public InstructorDto(){

    }

    public InstructorDto(int id, String experience){
        super(id);
        this.experience = experience;
    }

    public int getId(){
        return id;
    }

    public String getExperience(){
        return experience;
    }


}
