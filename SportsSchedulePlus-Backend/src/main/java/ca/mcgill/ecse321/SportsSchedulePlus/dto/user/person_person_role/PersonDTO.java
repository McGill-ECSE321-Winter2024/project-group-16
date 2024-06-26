package ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role;



import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.owner.OwnerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;

public class PersonDTO implements Comparable<PersonDTO>{

    private int id;
    private String name;
    private String email;
    private String password;
    private PersonRoleResponseDTO personRoleDto;
    private String userRole;
    private String applicationState;

    public PersonDTO(){

    }

    public PersonDTO (Person person, Owner owner) {
        OwnerResponseDTO ownerDTO = new OwnerResponseDTO(owner);
        this.id = person.getId();
        this.name = person.getName();
        this.email = person.getEmail();
        this.password = person.getPassword();
        this.personRoleDto = ownerDTO;
        this.userRole = "Owner";

    }

     public PersonDTO(int id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public PersonDTO(String name, String email, String password, PersonRoleResponseDTO personRoleDto){
        this.name = name;
        this.email = email;
        this.password = password;
        this.personRoleDto = personRoleDto;
        this.id = personRoleDto.getId();
    }

    public PersonDTO (Person person) {
        id = person.getId();
        name = person.getName();
        email = person.getEmail();
        password = person.getPassword();
        PersonRole personRole = person.getPersonRole();
        if (personRole != null) {
            if (personRole instanceof Customer) {
                personRoleDto = new CustomerRequestDTO((Customer) personRole);
                this.userRole = "Customer";
                Customer customer = ((Customer) personRole);
                this.applicationState = customer.getState().toString();
            } else if (personRole instanceof Instructor) {
                personRoleDto = new InstructorResponseDTO((Instructor) personRole);
                this.userRole = "Instructor";
            } else {
                personRoleDto = new OwnerResponseDTO((Owner) personRole);
                this.userRole = "Owner";
            }
        }
    }

    public int getId() {
        return id;
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

    public PersonRoleResponseDTO getPersonRoleDto(){
        return personRoleDto;
    }

    public  String getRole(){
        return this.userRole;
    }

    public String getApplicationState(){
        return this.applicationState;
    }

    @Override
    public int compareTo(PersonDTO o) {
        return Integer.compare(id, o.getId());
    }
}