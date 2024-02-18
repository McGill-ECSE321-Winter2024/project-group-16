package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;

// Interface for managing data related to Persons in the application
public interface PersonRepository extends CrudRepository<Person, Integer>{

    // Find persons by name
    List<Person> findPersonByName(String name);

    // Find person by email
    Person findPersonByEmail(String email);
}
