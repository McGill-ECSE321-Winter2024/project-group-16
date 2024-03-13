package ca.mcgill.ecse321.SportsSchedulePlus.dto;

public abstract class PersonRoleResponseDTO {

    private int id;

    public PersonRoleResponseDTO() {
    }

    public PersonRoleResponseDTO(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }



}
