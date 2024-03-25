package ca.mcgill.ecse321.SportsSchedulePlus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.owner.OwnerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonRoleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import java.util.ArrayList;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private CustomerRepository customerRepo;
    @Autowired
    private InstructorRepository instructorRepo;
    @Autowired
    private OwnerRepository ownerRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private PersonRoleRepository personRoleRepo;
    /*@Autowired
    private PersonDTO personDTO;
    @Autowired
    private PersonListResponseDTO personListResponseDTO;
    @Autowired 
    private PersonRoleResponseDTO personRoleResponseDTO;
    @Autowired
    private CustomerRequestDTO customerRequestDTO;
    @Autowired
    private InstructorResponseDTO instructorResponseDTO;
    @Autowired
    private OwnerResponseDTO ownerResponseDTO;*/


	@BeforeEach
	@AfterEach
	public void clearDatabase() {
        personRepo.deleteAll();
        personRoleRepo.deleteAll();
		customerRepo.deleteAll();
        instructorRepo.deleteAll();
        ownerRepo.deleteAll();
	}

	// Test creating and fetching a CourseType
    @Test
    public void testCreateAndGetCustomer() {
        int id = testCreateCustomer();
        testGetCustomer(id);
    }

    @Test
    public void testCreateAndGetInstructor() {
        String email = testCreateInstructor();
        testGetInstructor(email);
    }

    @Test
    public void testCreateAndGetOwner() {
        testCreateOwner();
        testGetOwner();
    }

    private int testCreateCustomer() {
        CustomerRequestDTO newCustomerRole = new CustomerRequestDTO();
        newCustomerRole.setHasApplied(true);
        ResponseEntity<PersonDTO> response = client.postForEntity("/customers", new PersonDTO("customer", "customer@example.com", "Password#1", newCustomerRole ), PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("customer", response.getBody().getName(), "Name matches");
        assertEquals("customer@example.com", response.getBody().getEmail(), "Email matches");
        assertTrue(response.getBody().getId() > 0, "Response has valid ID");

        return response.getBody().getId();
    }

    private PersonDTO postCustomer(String name, String email, String password) {
        ResponseEntity < PersonDTO > newCustomer = client.postForEntity("/customers", new PersonDTO(name, email, password, new CustomerRequestDTO(1)), PersonDTO.class);
        return newCustomer.getBody();
    }

    private String testCreateInstructor() {
        PersonDTO newCustomer = postCustomer("customer", "customer@email.com", "Password#1");
        client.put("/customers/{customerId}/approve", null, newCustomer.getId());

        ResponseEntity<PersonDTO> response = client.getForEntity("/instructors/customer@example.com", PersonDTO.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("instructor", response.getBody().getName(), "Name matches");
        assertEquals("instructor@email.com", response.getBody().getEmail(), "Email matches");
        assertTrue(response.getBody().getId() > 0, "Response has valid ID");

        return response.getBody().getEmail();
    }

    private void testCreateOwner() {
        Owner owner = new Owner();
        owner.setId(-1);
        owner.setDailySchedule(new ArrayList<>());
        OwnerResponseDTO newOwnerRole = new OwnerResponseDTO(owner);
        ResponseEntity<PersonDTO> response = client.postForEntity("/owner", new PersonDTO("owner", "sports.schedule.plus@gmail.com", "Password#1", newOwnerRole ), PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("owner", response.getBody().getName(), "Name matches");
        assertEquals("sports.schedule.plus@gmail.com", response.getBody().getEmail(), "Email matches");
        assertTrue(response.getBody().getId() > 0, "Response has valid ID");
    }

    private void testGetCustomer(int id) {
        ResponseEntity<PersonDTO> response = client.getForEntity("/customers/" + id, PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status is OK");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("customer", response.getBody().getName(), "Name matches");
        assertEquals("customer@example.com", response.getBody().getEmail(), "Email matches");
        assertEquals(id, response.getBody().getId(), "Id matches");
    }

    private void testGetInstructor(String email) {
        ResponseEntity<PersonDTO> response = client.getForEntity("/instructors/" + email, PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status is OK");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("instructor", response.getBody().getName(), "Name matches");
        assertEquals("instructor@email.com", response.getBody().getEmail(), "Email matches");
    }

    private void testGetOwner() {
        ResponseEntity<PersonDTO> response = client.getForEntity("/owner/", PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status is OK");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("owner", response.getBody().getName(), "Name matches");
        assertEquals("sports.schedule.plus@gmail.com", response.getBody().getEmail(), "Email matches");
    }
}

