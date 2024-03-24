package ca.mcgill.ecse321.SportsSchedulePlus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
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
    private PersonDTO personDTO;
    @Autowired
    private PersonListResponseDTO personListResponseDTO;
    @Autowired 
    private PersonRoleResponseDTO personRoleResponseDTO;


	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		customerRepo.deleteAll();
        instructorRepo.deleteAll();
        ownerRepo.deleteAll();
	}

	// Test creating and fetching a CourseType
    @Test
    public void testCreateAndGetCustomer() {
        int id = testCreateCustomer();
    }

    private int testCreateCustomer() {
        CustomerRequestDTO newCustomerRole = new CustomerRequestDTO(1);
        newCustomerRole.setHasApplied(true);
        ResponseEntity<PersonDTO> response = client.postForEntity("/customer", new PersonDTO("customer", "customer@example.com", "Password#1", newCustomerRole ), PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("customer", response.getBody().getName(), "Name matches");
        assertEquals("customer@example.com", response.getBody().getEmail(), "Email matches");
        assertEquals("encodedPassword", response.getBody().getPassword(), "Password matches");
        assertEquals(1, response.getBody().getId(), "Id matches");

        return response.getBody().getId();
    }

    private int testCreateInstructor() {
        Instructor instructor = new Instructor();
        instructor.setExperience("5 years");
        instructor.setHasApplied(true);
        instructor.setId(2);
        InstructorResponseDTO newInstructorRole = new InstructorResponseDTO(instructor);
        ResponseEntity<PersonDTO> response = client.postForEntity("/instructor", new PersonDTO("instructor", "instructor@email.com", "Password#1", newInstructorRole ), PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("instructor", response.getBody().getName(), "Name matches");
        assertEquals("instructor@email.com", response.getBody().getEmail(), "Email matches");
        assertEquals("encodedPassword", response.getBody().getPassword(), "Password matches");
        assertEquals(2, response.getBody().getId(), "Id matches");

        return response.getBody().getId();
    }

    private int testCreateOwner() {
        Owner owner = new Owner();
        owner.setId(-1);
        owner.setDailySchedule(new ArrayList<>());
        OwnerResponseDTO newOwnerRole = new OwnerResponseDTO(owner);
        ResponseEntity<PersonDTO> response = client.postForEntity("/owner", new PersonDTO("owner", "owner@email.com", "Password#1", newOwnerRole ), PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("owner", response.getBody().getName(), "Name matches");
        assertEquals("owner@email.com", response.getBody().getEmail(), "Email matches");
        assertEquals("encodedPassword", response.getBody().getPassword(), "Password matches");
        assertEquals(-1, response.getBody().getId(), "Id matches");

        return response.getBody().getId();
    }
}
