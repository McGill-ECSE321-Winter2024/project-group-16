package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonRoleRepository personRoleRepository;


    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
        personRoleRepository.deleteAll();
    }

    // Test case for reading and writing Person
    @Test
    public void testPersistAndLoadPerson() {
        // Create person.
        String name = "John Doe";
        String email = "john.doe@example.com";
        String password = "password123";
        PersonRole role = new Customer(1);      
        Person person = new Person(name, email, password, role);

        // Save role
        personRoleRepository.save(role);
        // Save person
        personRepository.save(person);
        

        // Read person from database.
        Person loadedPerson = personRepository.findPersonByEmail(email);
  

        // Assert that person is not null and has correct attributes.
        assertNotNull(loadedPerson);
        assertEquals(name, loadedPerson.getName());
        assertEquals(email, loadedPerson.getEmail());
        assertEquals(password, loadedPerson.getPassword());
        assertNotNull(loadedPerson.getPersonRole());
        assertEquals(role.getId(), loadedPerson.getPersonRole().getId());
    }

   
    
}