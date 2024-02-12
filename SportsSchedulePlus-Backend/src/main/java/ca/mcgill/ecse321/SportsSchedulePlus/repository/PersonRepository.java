package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
public interface PersonRepository extends CrudRepository<Person, Integer>{

    // Find payments by name
	List<Person> findPersonByName(String name);
    // Find payments by email
    Person findPersonByEmail(String email);
}