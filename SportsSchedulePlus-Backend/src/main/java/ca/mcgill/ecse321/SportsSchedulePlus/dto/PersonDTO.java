package ca.mcgill.ecse321.SportsSchedulePlus.dto;

public class PersonDTO {

    private String name;
    private String email;
    private String password;
    private PersonRoleDTO personRoleDto;

    public PersonDTO(){

    }

    public PersonDTO(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public PersonDTO(String name, String email, String password, PersonRoleDTO personRoleDto){
        personRoleDto.getId();
        this.name = name;
        this.email = email;
        this.password = password;
        this.personRoleDto = personRoleDto;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public PersonRoleDTO getPersonRoleDto(){
        return personRoleDto;
    }
}