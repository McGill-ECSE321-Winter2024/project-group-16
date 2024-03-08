package ca.mcgill.ecse321.SportsSchedulePlus.dto;

public abstract class PersonRoleDto {

    private int id;

    public PersonRoleDto() {
        // Default constructor
    }

    public PersonRoleDto(int id) {
        this.id = id;
    }

    // Getters and setters for each attribute

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
