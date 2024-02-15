package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;

@SpringBootTest
public class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonRoleRepository personRoleRepository;

    // Clear the database after each test
    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
        personRoleRepository.deleteAll();
    }

    // Test finding a person by name
    @Test
    public void testFindPersonByName() {
        // Create a PersonRole
        PersonRole personRole = new Customer(1);
        personRoleRepository.save(personRole);

        // Create a new person with a specific name, email, password, and associated PersonRole
        Person newPerson = new Person("Ana", "ana@example.com", "password", personRole);
        personRepository.save(newPerson);

        // When finding persons by name
        List<Person> foundPersons = personRepository.findPersonByName("Ana");

        // Then ensure the list is not null, has one element, and the found person's name matches the expected
        assertNotNull(foundPersons);
        assertEquals(1, foundPersons.size());
        Person foundPerson = foundPersons.get(0);

        assertEquals(newPerson.getName(), foundPerson.getName());
    }

    // Test finding a person by email
    @Test
    public void testFindPersonByEmail() {
        // Create a PersonRole
        PersonRole personRole = new Customer(1);
        personRoleRepository.save(personRole);

        // Create a new person with a specific name, email, password, and associated PersonRole
        Person newPerson = new Person("John", "john@example.com", "password", personRole);
        personRepository.save(newPerson);

        // When finding a person by email
        Person foundPerson = personRepository.findPersonByEmail("john@example.com");

        // Then ensure the found person is not null and the email matches the expected
        assertNotNull(foundPerson);
        assertEquals(newPerson.getEmail(), foundPerson.getEmail());
    }

    // Test finding a person by email when the person is not found
    @Test
    public void testFindPersonByEmailNotFound() {
        // When finding a person by a nonexistent email
        Person foundPerson = personRepository.findPersonByEmail("nonexistent@example.com");

        // Then ensure the found person is null
        assertNull(foundPerson);
    }
}
