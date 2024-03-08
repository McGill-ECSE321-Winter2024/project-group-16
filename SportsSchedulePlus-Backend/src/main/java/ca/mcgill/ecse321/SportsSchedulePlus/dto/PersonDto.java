package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;

public class PersonDto {

    private String name;
    private String email;
    private String password;
    private PersonRoleDto personRoleDto;

    public PersonDto(){

    }

    public PersonDto(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public PersonDto(String name, String email, String password, PersonRoleDto personRoleDto){
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

    public PersonRoleDto getPersonRoleDto(){
        return personRoleDto;
    }
}
