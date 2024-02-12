package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
public interface PersonRepository extends CrudRepository<Person, String>{

    // Find payments by name
	Person findPersonByName(String name);
    // Find payments by email
    Person findPersonByEmail(String email);
}