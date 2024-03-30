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
import ca.mcgill.ecse321.utils.Helper;

/**
 * This class contains unit tests for the PersonRepository.
 * The overridden equals method in the Person model is used for assertions.
 */
@SpringBootTest
public class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonRoleRepository personRoleRepository;

    /**
     * Clear the database after each test.
     */
    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
        personRoleRepository.deleteAll();
    }

    /**
     * Test finding a person by name.
     */
    @Test
    public void testFindPersonByName() {
        // Create a PersonRole
        PersonRole personRole = new Customer();
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
        
        // The overridden equals method in the Person model is used here
        assertEquals(newPerson, foundPerson);
    }

    /**
     * Test finding a person by email.
     */
    @Test
    public void testFindPersonByEmail() {
        // Create a PersonRole
        PersonRole personRole = new Customer();
        personRoleRepository.save(personRole);

        // Create a new person with a specific name, email, password, and associated PersonRole
        Person newPerson = new Person("John", "johnny@example.com", "password", personRole);
        personRepository.save(newPerson);
        

        // When finding a person by email
        Person foundPerson = personRepository.findPersonByEmail("johnny@example.com");

        // Then ensure the found person is not null and the email matches the expected
        assertNotNull(foundPerson);
        
        // The overridden equals method in the Person model is used here
        assertEquals(newPerson, foundPerson);
    }

    /**
     * Test finding a person by email when the person is not found.
     */
    @Test
    public void testFindPersonByEmailNotFound() {
        // When finding a person by a nonexistent email
        Person foundPerson = personRepository.findPersonByEmail("nonexistent@example.com");

        // Then ensure the found person is null
        assertNull(foundPerson);
    }

    /**
     * Test finding a person by PersonRole.
     */
    @Test
    public void testFindPersonByPersonRole() {
        // Create a PersonRole
        PersonRole personRole = new Customer();
        personRoleRepository.save(personRole);

        // Create a new person with a specific name, email, password, and associated PersonRole
        Person newPerson = new Person("John", "alex@example.com", "password", personRole);
        personRepository.save(newPerson);
        

        // When finding a person by email
        Person foundPerson = personRepository.findPersonByPersonRole(personRole);

        // Then ensure the found person is not null and the email matches the expected
        assertNotNull(foundPerson);
        
        // The overridden equals method in the Person model is used here
        assertEquals(newPerson, foundPerson);
    }

     /**
     * Test finding a person by a nonexistent name.
     */
    @Test
    public void testFindPersonByNameNotFound() {
        // When finding a person by a nonexistent name
        List<Person> foundPersons = personRepository.findPersonByName("Nonexistent");

        // Then ensure the list is empty
        assertEquals(0, foundPersons.size());
    }

    /**
     * Test deleting a person by email.
     */
    @Test
    public void testDeletePersonByEmail() {
        // Create a PersonRole
        PersonRole personRole = new Customer();
        personRoleRepository.save(personRole);

        // Create a new person with a specific name, email, password, and associated PersonRole
        Person newPerson = new Person("John", "john-smith@example.com", "password", personRole);
        personRepository.save(newPerson);

        // Delete the person by email
        personRepository.deleteByEmail("john@example.com");

        // Verify if the person is deleted by trying to find it again by email
        Person deletedPerson = personRepository.findPersonByEmail("john@example.com");
        
        // Ensure the deleted person is null
        assertNull(deletedPerson);
    }

    /**
     * Test deleting a non-existent person by email.
     */
    @Test
    public void testDeleteNonExistentPersonByEmail() {
        // Attempt to delete a person with a non-existent email
        personRepository.deleteByEmail("nonexistent@example.com");

        // Verify if any changes occurred in the database
        List<Person> allPersons = Helper.toList(personRepository.findAll());

        // Ensure the database remains unchanged
        assertEquals(0, allPersons.size());
    }


  
}
